import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.CharacterCodingException;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/** Tests framing boundaries, partial input and strict decoding. @author Snehanjan Chatterjee */
public final class CoreFramedTextTest {
    @Test public void testFragmentAndMultipleFrames() throws CharacterCodingException {
        final ByteBuffer input = ByteBuffer.allocate(64);
        input.putInt(3).put((byte) 0xE2).flip();
        assertTrue(CoreFramedText.extract(input).isEmpty()); assertEquals(0, input.position());
        input.compact(); input.put((byte) 0x82).put((byte) 0xAC).putInt(1).put((byte) 'x').flip();
        assertEquals(Arrays.asList("\u20AC", "x"), CoreFramedText.extract(input)); assertEquals(0, input.remaining());
    }
    @Test public void testShortHeaderEmptyBodyAndErrors() throws CharacterCodingException {
        final ByteBuffer shortHeader = ByteBuffer.wrap(new byte[] {0, 0});
        assertTrue(CoreFramedText.extract(shortHeader).isEmpty()); assertEquals(0, shortHeader.position());
        final ByteBuffer empty = ByteBuffer.allocate(4); empty.putInt(0).flip();
        assertEquals(Arrays.asList(""), CoreFramedText.extract(empty));
        final ByteBuffer negative = ByteBuffer.allocate(4); negative.putInt(-1).flip();
        assertThrows(IllegalArgumentException.class, () -> CoreFramedText.extract(negative));
        final ByteBuffer oversized = ByteBuffer.allocate(4); oversized.putInt(1025).flip();
        assertThrows(IllegalArgumentException.class, () -> CoreFramedText.extract(oversized));
        final ByteBuffer malformed = ByteBuffer.allocate(5); malformed.putInt(1).put((byte) 0xFF).flip();
        assertThrows(CharacterCodingException.class, () -> CoreFramedText.extract(malformed));
        assertThrows(NullPointerException.class, () -> CoreFramedText.extract(null));
        assertThrows(IllegalArgumentException.class, () -> CoreFramedText.extract(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN)));
    }
    @Test public void testEncodeAndFinish() throws CharacterCodingException {
        final ByteBuffer frame = CoreFramedText.encode("\u20AC");
        assertEquals(3, frame.getInt(0));
        assertEquals(Arrays.asList("\u20AC"), CoreFramedText.finish(frame));
        assertEquals(Arrays.asList(""), CoreFramedText.finish(CoreFramedText.encode("")));
        assertTrue(CoreFramedText.finish(ByteBuffer.allocate(0)).isEmpty());
        assertThrows(IllegalArgumentException.class, () -> CoreFramedText.finish(ByteBuffer.wrap(new byte[] {0})));
        final ByteBuffer partial = ByteBuffer.allocate(5); partial.putInt(3).put((byte) 'x').flip();
        assertThrows(IllegalArgumentException.class, () -> CoreFramedText.finish(partial));
        assertThrows(NullPointerException.class, () -> CoreFramedText.encode(null));
        assertThrows(CharacterCodingException.class, () -> CoreFramedText.encode("\uD800"));
        final char[] large = new char[1025]; Arrays.fill(large, 'a');
        assertThrows(IllegalArgumentException.class, () -> CoreFramedText.encode(new String(large)));
    }
    @Test public void testUtf8ByteLimitDiffersFromCodeUnitLimit() throws CharacterCodingException {
        final char[] ascii = new char[1024]; Arrays.fill(ascii, 'a');
        assertEquals(Arrays.asList(new String(ascii)), CoreFramedText.finish(CoreFramedText.encode(new String(ascii))));
        final char[] multibyte = new char[342]; Arrays.fill(multibyte, '\u20AC');
        assertThrows(IllegalArgumentException.class, () -> CoreFramedText.encode(new String(multibyte)));
        final String exact = new String(multibyte, 0, 341) + "a";
        assertEquals(Arrays.asList(exact), CoreFramedText.finish(CoreFramedText.encode(exact)));
    }
}
