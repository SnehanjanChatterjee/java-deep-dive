import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/** Checks typed storage, replacement and the explicit null policy. @author Snehanjan Chatterjee */
public final class GenericBoxTest {
    @Test public void testTypedReadAndReplacement() {
        final GenericBox<String> text = new GenericBox<>("first");
        text.set("second"); assertEquals("second", text.get());
        final GenericBox<Integer> number = new GenericBox<>(7);
        assertEquals(Integer.valueOf(7), number.get());
    }
    @Test public void testNullIsPermitted() {
        final GenericBox<String> text = new GenericBox<>(null);
        assertNull(text.get()); text.set("present"); text.set(null); assertNull(text.get());
    }
}
