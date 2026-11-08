import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;

/** Shows a stateful Reader spanning one-byte transport chunks. @author Snehanjan Chatterjee */
public final class EncodingBoundaryLab {
    private static final class OneByteInput extends ByteArrayInputStream {
        /** Uses only caller-supplied small in-memory data. */
        private OneByteInput(final byte[] bytes) { super(bytes); }
        /** Restricts each bulk read to one byte to expose split UTF-8 code points. */
        @Override public synchronized int read(final byte[] bytes, final int offset, final int length) {
            return super.read(bytes, offset, Math.min(length, 1));
        }
    }

    /** Compares UTF-16 units, code points and bytes, then rejects malformed UTF-16. */
    public static void main(final String[] args) throws IOException {
        final String symbol = "\uD83D\uDE00";
        final byte[] bytes = symbol.getBytes(StandardCharsets.UTF_8);
        System.out.println("units=" + symbol.length() + ", points=" + symbol.codePointCount(0, symbol.length()) + ", bytes=" + bytes.length);
        final StringBuilder result = new StringBuilder();
        try (final Reader reader = new InputStreamReader(new OneByteInput(bytes), StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT))) {
            final char[] unit = new char[1];
            for (int read; (read = reader.read(unit)) != -1;) { result.append(unit, 0, read); }
        }
        System.out.println("reconstructed=" + symbol.equals(result.toString()));
        try {
            StandardCharsets.UTF_8.newEncoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT)
                .encode(CharBuffer.wrap("\uD800"));
        } catch (final CharacterCodingException expected) { System.out.println("surrogate rejected"); }
    }
}
