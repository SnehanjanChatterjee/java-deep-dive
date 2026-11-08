import org.junit.Test;
import static org.junit.Assert.assertEquals;

/** Checks operand order and ordinary wrapping arithmetic. */
public final class ExecutionFramesDemoTest {
    @Test public void testTwiceAndOverflow() {
        assertEquals(6, ExecutionFramesDemo.twice(3));
        assertEquals(-6, ExecutionFramesDemo.twice(-3));
        assertEquals(0, ExecutionFramesDemo.twice(0));
        assertEquals(Integer.MIN_VALUE, ExecutionFramesDemo.twice(1 << 30));
    }
    @Test public void testDifferenceOperandOrderAndBoundaries() {
        assertEquals(5, ExecutionFramesDemo.difference(9, 4));
        assertEquals(-5, ExecutionFramesDemo.difference(4, 9));
        assertEquals(0, ExecutionFramesDemo.difference(4, 4));
        assertEquals(Integer.MAX_VALUE, ExecutionFramesDemo.difference(Integer.MIN_VALUE, 1));
    }
}
