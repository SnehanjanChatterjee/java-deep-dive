import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Reads complete length-prefixed UTF-8 frames while retaining incomplete input. @author Snehanjan Chatterjee */
public final class CoreFramedText {
    private static final int MAX_BYTES = 1024;
    /** Consumes complete frames; malformed input fails without promising batch rollback. */
    public static List<String> extract(final ByteBuffer input) throws CharacterCodingException {
        Objects.requireNonNull(input, "input");
        if (input.order() != ByteOrder.BIG_ENDIAN) { throw new IllegalArgumentException("big-endian prefix required"); }
        final List<String> completed = new ArrayList<>();
        while (input.remaining() >= Integer.BYTES) {
            input.mark();
            final int length = input.getInt();
            if (length < 0 || length > MAX_BYTES) { throw new IllegalArgumentException("invalid frame length"); }
            if (input.remaining() < length) { input.reset(); break; }
            final ByteBuffer body = input.slice();
            body.limit(length);
            final String text = StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT).decode(body).toString();
            input.position(input.position() + length);
            completed.add(text);
        }
        return completed;
    }
    /** Demonstrates a header and multi-byte character split across two input chunks. */
    public static void main(final String[] args) throws CharacterCodingException {
        final ByteBuffer input = ByteBuffer.allocate(32);
        input.putInt(3).put((byte) 0xE2).flip();
        System.out.println(extract(input).size());
        input.compact();
        input.put((byte) 0x82).put((byte) 0xAC).flip();
        final List<String> result = extract(input);
        System.out.println(result.size());
        System.out.println(result.get(0).codePointAt(0));
        System.out.println(input.remaining());
    }
}
