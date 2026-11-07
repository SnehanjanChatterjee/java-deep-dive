import java.util.NoSuchElementException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/** Checks endpoint repair, indexes and null membership. @author Snehanjan Chatterjee */
public final class DoublyLinkedListTest {
    @Test public void testInsertionRemovalAndReuse() {
        final DoublyLinkedList<Integer> list = new DoublyLinkedList<>();
        list.add(0, 2); list.add(0, 1); list.addLast(4); list.add(2, 3);
        for (int i = 0; i < 4; i++) { assertEquals(Integer.valueOf(i + 1), list.get(i)); }
        assertEquals(Integer.valueOf(3), list.remove(2));
        assertEquals(Integer.valueOf(4), list.remove(2));
        assertEquals(Integer.valueOf(1), list.removeFirst());
        assertEquals(Integer.valueOf(2), list.removeFirst());
        assertEquals(0, list.size()); list.addLast(null); assertNull(list.removeFirst());
        list.addLast(9); assertEquals(Integer.valueOf(9), list.get(0));
    }
    @Test public void testInvalidOperationsPreserveState() {
        final DoublyLinkedList<Integer> list = new DoublyLinkedList<>();
        assertThrows(NoSuchElementException.class, list::removeFirst);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(1, 1));
        list.addLast(7); assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
        assertEquals(1, list.size()); assertEquals(Integer.valueOf(7), list.get(0));
    }
}
