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
}
