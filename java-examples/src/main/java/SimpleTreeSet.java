import java.util.List;

/** Delegates sorted membership to the preceding unbalanced search tree. @author Snehanjan Chatterjee */
public final class SimpleTreeSet<T extends Comparable<? super T>> {
    private static final Object PRESENT = new Object();
    private final SimpleBinarySearchTree<T, Object> tree = new SimpleBinarySearchTree<>();
    /** Adds a comparison-distinct element and reports whether membership changed. */
    public boolean add(final T value) { return tree.put(value, PRESENT) == null; }
    /** Tests membership using the tree's comparison relation. */
    public boolean contains(final T value) { return tree.containsKey(value); }
    /** Removes a present element and reports whether membership changed. */
    public boolean remove(final T value) { return tree.remove(value) != null; }
    /** Returns the number of live comparison-distinct elements. */
    public int size() { return tree.size(); }
    /** Copies elements in their natural order without copying the element objects. */
    public List<T> asList() { return tree.keysInOrder(); }
    /** Demonstrates duplicate rejection, sorted traversal and removal. */
    public static void main(final String[] args) {
        final SimpleTreeSet<Integer> set = new SimpleTreeSet<>();
        set.add(30); set.add(10); set.add(20);
        System.out.println(set.add(10));
        System.out.println(set.asList());
        set.remove(20);
        System.out.println(set.asList());
    }
}
