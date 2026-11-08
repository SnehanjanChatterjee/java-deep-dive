import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/** Checks ordered mappings and every structural deletion category. @author Snehanjan Chatterjee */
public final class SimpleBinarySearchTreeTest {
    @Test public void testEmptyReplacementAndNullValue() {
        final SimpleBinarySearchTree<Integer, String> tree = new SimpleBinarySearchTree<>();
        assertEquals(0, tree.height());
        assertEquals(Collections.emptyList(), tree.keysInOrder());
        assertNull(tree.get(1));
        assertNull(tree.remove(1));
        assertFalse(tree.containsKey(1));
        assertNull(tree.put(2, null));
        assertTrue(tree.containsKey(2));
        assertNull(tree.put(2, "two"));
        assertEquals("two", tree.put(2, "replacement"));
        assertEquals(1, tree.size());
        assertEquals("replacement", tree.get(2));
        assertEquals("replacement", tree.remove(2));
        assertEquals(0, tree.size());
    }
    @Test public void testLeafOneChildAndTwoChildRemoval() {
        final SimpleBinarySearchTree<Integer, String> tree = new SimpleBinarySearchTree<>();
        for (final int key : new int[] {20, 10, 30, 5, 15, 25, 40, 27}) { tree.put(key, "v" + key); }
        assertEquals("v5", tree.remove(5));
        assertEquals("v25", tree.remove(25));
        assertEquals("v20", tree.remove(20));
        assertEquals(Arrays.asList(10, 15, 27, 30, 40), tree.keysInOrder());
        assertEquals(5, tree.size());
        assertFalse(tree.containsKey(20));
        assertEquals("v27", tree.get(27));
        assertNull(tree.remove(100));
    }
    @Test public void testOppositeSingleChildAndReuse() {
        final SimpleBinarySearchTree<Integer, Integer> tree = new SimpleBinarySearchTree<>();
        tree.put(2, 2); tree.put(1, 1);
        assertEquals(Integer.valueOf(2), tree.remove(2));
        assertEquals(Arrays.asList(1), tree.keysInOrder());
        tree.remove(1); tree.put(3, 3);
        assertEquals(Integer.valueOf(3), tree.get(3));
        final List<Integer> keys = tree.keysInOrder();
        keys.clear();
        assertEquals(1, tree.size());
    }
    @Test public void testSuccessorKeepsItsRightChild() {
        final SimpleBinarySearchTree<Integer, String> tree = new SimpleBinarySearchTree<>();
        for (final int key : new int[] {20, 10, 30, 25, 27, 40}) { tree.put(key, "v" + key); }
        assertEquals("v20", tree.remove(20));
        assertEquals(Arrays.asList(10, 25, 27, 30, 40), tree.keysInOrder());
        assertEquals("v27", tree.get(27)); assertEquals(5, tree.size());
    }
    @Test public void testDegenerateHeightAndNullKeys() {
        final SimpleBinarySearchTree<Integer, String> tree = new SimpleBinarySearchTree<>();
        for (final int key : new int[] {1, 2, 3, 4}) { tree.put(key, "v" + key); }
        assertEquals(4, tree.height());
        assertThrows(NullPointerException.class, () -> tree.put(null, "value"));
        assertThrows(NullPointerException.class, () -> tree.get(null));
        assertThrows(NullPointerException.class, () -> tree.remove(null));
        assertThrows(NullPointerException.class, () -> tree.containsKey(null));
    }
}
