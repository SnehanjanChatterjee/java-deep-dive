import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

/** Checks immutable snapshot validation and replacement boundaries. @author Snehanjan Chatterjee */
public final class VolatilePublicationLabTest {
    @Test public void testInitialAndReplacedSnapshots() {
        final VolatilePublicationLab store = new VolatilePublicationLab(); assertNull(store.read());
        final VolatilePublicationLab.Snapshot first = new VolatilePublicationLab.Snapshot(40, 2);
        store.publish(first); assertSame(first, store.read()); assertEquals(42, store.read().total());
        store.publish(new VolatilePublicationLab.Snapshot(1, 2)); assertEquals(3, store.read().total()); assertEquals(42, first.total());
        assertThrows(NullPointerException.class, () -> store.publish(null)); assertEquals(3, store.read().total());
    }
    @Test public void testInvalidAndOverflowValues() {
        assertThrows(IllegalArgumentException.class, () -> new VolatilePublicationLab.Snapshot(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> new VolatilePublicationLab.Snapshot(0, -1));
        assertThrows(ArithmeticException.class, () -> new VolatilePublicationLab.Snapshot(Integer.MAX_VALUE, 1));
        assertEquals(0, new VolatilePublicationLab.Snapshot(0, 0).total());
    }
}
