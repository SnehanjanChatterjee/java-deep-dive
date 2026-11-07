import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Combines a permitted hash index with handwritten insertion-order links. @author Snehanjan Chatterjee */
public final class SimpleLinkedHashSet<T> {
    private static final class Node<T> {
        private final T value;
        private Node<T> previous;
        private Node<T> next;
        private Node(final T value) { this.value = value; }
    }
    private final Map<T, Node<T>> index = new HashMap<>();
    private Node<T> first;
    private Node<T> last;
    /** Adds a previously absent member at the end; duplicates retain position. */
    public boolean add(final T value) {
        if (index.containsKey(value)) { return false; }
        final Node<T> node = new Node<>(value);
        index.put(value, node);
        node.previous = last;
        if (last == null) { first = node; } else { last.next = node; }
        last = node;
        return true;
    }
    /** Tests membership without changing encounter order. */
    public boolean contains(final T value) { return index.containsKey(value); }
    /** Removes a member from both the index and order chain. */
    public boolean remove(final T value) {
        final Node<T> node = index.remove(value);
        if (node == null) { return false; }
        if (node.previous == null) { first = node.next; } else { node.previous.next = node.next; }
        if (node.next == null) { last = node.previous; } else { node.next.previous = node.previous; }
        node.previous = null;
        node.next = null;
        return true;
    }
    /** Returns the number of live indexed nodes. */
    public int size() { return index.size(); }
    /** Copies the order into an independent container, sharing element objects. */
    public List<T> asList() {
        final List<T> result = new ArrayList<>();
        for (Node<T> node = first; node != null; node = node.next) { result.add(node.value); }
        return result;
    }
    /** Demonstrates duplicate insertion and remove-then-reinsert. */
    public static void main(final String[] args) {
        final SimpleLinkedHashSet<String> set = new SimpleLinkedHashSet<>();
        set.add("B"); set.add("A"); set.add("B"); set.add("C");
        System.out.println(set.asList());
        set.remove("A"); set.add("A");
        System.out.println(set.asList());
    }
}
