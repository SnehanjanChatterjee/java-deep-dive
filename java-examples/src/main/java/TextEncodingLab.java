import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/** Distinguishes character counts, encoded byte counts and malformed input. @author Snehanjan Chatterjee */
public final class TextEncodingLab {
    /** Decodes one complete bounded UTF-8 byte sequence without replacement characters. */
    public static String decodeStrict(final byte[] bytes) throws CharacterCodingException {
        Objects.requireNonNull(bytes, "bytes");
        return StandardCharsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT).decode(ByteBuffer.wrap(bytes)).toString();
    }
    /** Shows why a chunk containing only the start of a multibyte value is incomplete. */
    public static void main(final String[] args) throws CharacterCodingException {
        final String text = "\u20ac";
        final byte[] encoded = text.getBytes(StandardCharsets.UTF_8);
        System.out.println(text.length());
        System.out.println(encoded.length);
        System.out.println(decodeStrict(encoded));
        try { decodeStrict(new byte[] {(byte) 0xE2}); }
        catch (final CharacterCodingException expected) { System.out.println("incomplete UTF-8 rejected"); }
    }
}
