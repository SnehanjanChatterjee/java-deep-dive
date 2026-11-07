import java.util.Objects;

/** A single-file interview exercise using separate chaining. @author Snehanjan Chatterjee */
public final class MyHashMap<K, V> {
    private static final int MAX_CAPACITY = 1 << 20;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] buckets;
    private int size;
    private int threshold;

    /** Stores one mapping and the remainder of its collision chain. */
    private static final class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        /** Creates a bucket entry. */
        private Node(final int hash, final K key, final V value, final Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    /** Starts with sixteen eagerly allocated buckets. */
    public MyHashMap() {
        buckets = newTable(16);
        threshold = 12;
    }

    /** Creates the generic bucket array with one localized unchecked cast. */
    @SuppressWarnings("unchecked")
    private Node<K, V>[] newTable(final int capacity) {
        return (Node<K, V>[]) new Node<?, ?>[capacity];
    }

    /** Handles null and spreads high hash bits into the low bits. */
    private int hash(final Object key) {
        final int h = key == null ? 0 : key.hashCode();
        return h ^ (h >>> 16);
    }

    /** Finds an equal key within the selected bucket. */
    private Node<K, V> find(final int hash, final Object key) {
        final int index = hash & (buckets.length - 1);
        for (Node<K, V> node = buckets[index]; node != null; node = node.next) {
            if (node.hash == hash && Objects.equals(key, node.key)) {
                return node;
            }
        }
        return null;
    }

    /** Returns the value or null; use containsKey to distinguish a stored null. */
    public V get(final K key) {
        final Node<K, V> node = find(hash(key), key);
        return node == null ? null : node.value;
    }

    /** Reports presence independently of the stored value. */
    public boolean containsKey(final K key) {
        return find(hash(key), key) != null;
    }

    /** Adds or replaces a mapping and returns its previous value. */
    public V put(final K key, final V value) {
        final int hash = hash(key);
        final Node<K, V> existing = find(hash, key);
        if (existing != null) {
            final V oldValue = existing.value;
            existing.value = value;
            return oldValue;
        }
        final int index = hash & (buckets.length - 1);
        buckets[index] = new Node<>(hash, key, value, buckets[index]);
        size++;
        if (size > threshold) {
            resize();
        }
        return null;
    }

    /** Removes a mapping without losing the remaining collision chain. */
    public V remove(final K key) {
        final int hash = hash(key);
        final int index = hash & (buckets.length - 1);
        Node<K, V> previous = null;
        Node<K, V> current = buckets[index];
        while (current != null) {
            if (current.hash == hash && Objects.equals(key, current.key)) {
                if (previous == null) {
                    buckets[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
                return current.value;
            }
            previous = current;
            current = current.next;
        }
        return null;
    }

    /** Returns mapping count rather than the number of buckets. */
    public int size() {
        return size;
    }

    /** Doubles the array and redistributes existing nodes using stored hashes. */
    private void resize() {
        if (buckets.length == MAX_CAPACITY) {
            threshold = Integer.MAX_VALUE; // exercise bound, not a JDK guarantee
            return;
        }
        final Node<K, V>[] oldBuckets = buckets;
        final Node<K, V>[] newBuckets = newTable(oldBuckets.length << 1);
        for (final Node<K, V> head : oldBuckets) {
            Node<K, V> current = head;
            while (current != null) {
                final Node<K, V> next = current.next;
                final int index = current.hash & (newBuckets.length - 1);
                current.next = newBuckets[index];
                newBuckets[index] = current;
                current = next;
            }
        }
        buckets = newBuckets;
        threshold = (int) (buckets.length * LOAD_FACTOR);
    }

    /** Demonstrates collisions, replacement, removal, nulls and growth in one file. */
    public static void main(final String[] args) {
        final MyHashMap<Integer, String> map = new MyHashMap<>();
        map.put(1, "one");
        map.put(17, "seventeen"); // same bucket as 1 at capacity 16
        map.put(33, "thirty-three");
        System.out.println(map.get(17));
        System.out.println(map.put(17, "updated"));
        System.out.println(map.size()); // replacement does not increase size
        System.out.println(map.remove(1));
        System.out.println(map.get(33)); // other colliding entry survives
        map.put(null, null);
        System.out.println(map.containsKey(null));
        for (int i = 100; i < 140; i++) {
            map.put(i, "score-" + i);
        }
        System.out.println(map.get(139)); // survives growth
        System.out.println(map.get(999)); // missing
    }
}
