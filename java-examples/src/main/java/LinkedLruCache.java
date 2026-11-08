import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** A nonconcurrent LRU cache with an explicit doubly linked recency list. @author Snehanjan Chatterjee */
public final class LinkedLruCache<K, V> {
    private static final class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> previous;
        private Node<K, V> next;
        /** Creates a real entry or an internal sentinel. */
        private Node(final K key, final V value) { this.key = key; this.value = value; }
    }
    private final Map<K, Node<K, V>> index = new HashMap<>();
    private final Node<K, V> head = new Node<>(null, null);
    private final Node<K, V> tail = new Node<>(null, null);
    private final int capacity;

    /** Creates a positive-capacity cache rejecting null keys and values. */
    public LinkedLruCache(final int capacity) {
        if (capacity < 1) { throw new IllegalArgumentException("positive capacity required"); }
        this.capacity = capacity;
        head.next = tail;
        tail.previous = head;
    }

    /** Returns the value and records an access, or null for absence. */
    public V get(final K key) {
        final Node<K, V> node = index.get(Objects.requireNonNull(key, "key"));
        if (node == null) { return null; }
        unlink(node);
        append(node);
        return node.value;
    }

    /** Inserts or replaces a value and evicts the oldest entry after new insertion. */
    public void put(final K key, final V value) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(value, "value");
        final Node<K, V> existing = index.get(key);
        if (existing != null) {
            existing.value = value;
            unlink(existing);
            append(existing);
            return;
        }
        final Node<K, V> added = new Node<>(key, value);
        index.put(key, added);
        append(added);
        if (index.size() > capacity) {
            final Node<K, V> oldest = head.next;
            index.remove(oldest.key);
            unlink(oldest);
        }
    }

    /** Reports live mappings. */
    public int size() { return index.size(); }

    /** Reconnects both neighbors and clears a detached node's former links. */
    private void unlink(final Node<K, V> node) {
        node.previous.next = node.next;
        node.next.previous = node.previous;
        node.previous = null;
        node.next = null;
    }

    /** Links a detached node as most recently used. */
    private void append(final Node<K, V> node) {
        node.previous = tail.previous;
        node.next = tail;
        tail.previous.next = node;
        tail.previous = node;
    }

    /** Traces access-driven eviction. */
    public static void main(final String[] args) {
        final LinkedLruCache<String, Integer> cache = new LinkedLruCache<>(2);
        cache.put("A", 1); cache.put("B", 2);
        System.out.println(cache.get("A"));
        cache.put("C", 3);
        System.out.println(cache.get("B"));
        System.out.println(cache.get("C"));
        System.out.println(cache.size());
    }
}
