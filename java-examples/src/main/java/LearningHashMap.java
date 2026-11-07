/*
 * Java Deep Dive educational examples.
 * Author: Snehanjan Chatterjee.
 */
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

/**
 * A complete, runnable separate-chaining Map for studying hash-table mechanics.
 * This is original teaching code, not a copy of OpenJDK HashMap.
 * Tree bins, serialization and concurrency are intentionally outside its scope.
 * @author Snehanjan Chatterjee
 */
public final class LearningHashMap<K, V> extends AbstractMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int MAX_CAPACITY = 1 << 20;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private int modCount;
    private final Set<Map.Entry<K, V>> entries = new EntrySet();

    /** Creates an empty map with sixteen eagerly allocated buckets. */
    public LearningHashMap() {
        this(DEFAULT_CAPACITY);
    }

    /** Creates a map, rounding a positive requested capacity up to a power of two. */
    public LearningHashMap(final int requestedCapacity) {
        if (requestedCapacity < 1 || requestedCapacity > MAX_CAPACITY) {
            throw new IllegalArgumentException("Capacity must be between 1 and " + MAX_CAPACITY);
        }
        int capacity = 1;
        while (capacity < requestedCapacity) {
            capacity <<= 1;
        }
        table = newTable(capacity);
        threshold = thresholdFor(capacity);
    }

    /** Allocates the generic bucket array; its erased component type is Node. */
    @SuppressWarnings("unchecked")
    private static <K, V> Node<K, V>[] newTable(final int capacity) {
        return (Node<K, V>[]) new Node<?, ?>[capacity];
    }

    /** Mixes high hash bits into the low bits used by the bucket mask. */
    private static int spread(final Object key) {
        final int hash = key == null ? 0 : key.hashCode();
        return hash ^ (hash >>> 16);
    }

    /** Caps growth for this exercise; this bound is smaller than OpenJDK's. */
    private static int thresholdFor(final int capacity) {
        return capacity == MAX_CAPACITY ? Integer.MAX_VALUE : (int) (capacity * LOAD_FACTOR);
    }

    /** Returns the mapping count, not the bucket count. */
    @Override
    public int size() {
        return size;
    }

    /** Returns the value or null; containsKey distinguishes a stored null. */
    @Override
    public V get(final Object key) {
        final Node<K, V> node = find(spread(key), key);
        return node == null ? null : node.value;
    }

    /** Checks key presence independently of the associated value. */
    @Override
    public boolean containsKey(final Object key) {
        return find(spread(key), key) != null;
    }

    /** Searches one chain using the stored hash and key equality. */
    private Node<K, V> find(final int hash, final Object key) {
        for (Node<K, V> node = table[hash & (table.length - 1)]; node != null; node = node.next) {
            if (node.hash == hash && Objects.equals(key, node.key)) {
                return node;
            }
        }
        return null;
    }

    /** Adds or replaces a mapping and returns its old value, if any. */
    @Override
    public V put(final K key, final V value) {
        final int hash = spread(key);
        final Node<K, V> existing = find(hash, key);
        if (existing != null) {
            return existing.setValue(value); // replacement is not structural
        }
        final int index = hash & (table.length - 1);
        table[index] = new Node<>(hash, key, value, table[index]);
        size++;
        modCount++;
        if (size > threshold) {
            resize();
        }
        return null;
    }

    /** Doubles capacity and relinks existing nodes using their cached hashes. */
    private void resize() {
        final Node<K, V>[] oldTable = table;
        final Node<K, V>[] newTable = newTable(oldTable.length << 1);
        for (final Node<K, V> head : oldTable) {
            Node<K, V> node = head;
            while (node != null) {
                final Node<K, V> next = node.next; // save before rewriting the link
                final int index = node.hash & (newTable.length - 1);
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
        threshold = thresholdFor(table.length);
    }

    /** Removes a mapping, returning its old value or null. */
    @Override
    public V remove(final Object key) {
        final Node<K, V> removed = unlink(spread(key), key);
        return removed == null ? null : removed.value;
    }

    /** Unlinks a head, middle, or tail node without breaking the chain. */
    private Node<K, V> unlink(final int hash, final Object key) {
        final int index = hash & (table.length - 1);
        Node<K, V> previous = null;
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.hash == hash && Objects.equals(key, node.key)) {
                if (previous == null) {
                    table[index] = node.next;
                } else {
                    previous.next = node.next;
                }
                size--;
                modCount++;
                return node;
            }
            previous = node;
            node = node.next;
        }
        return null;
    }

    /** Clears mappings while retaining the allocated table. */
    @Override
    public void clear() {
        if (size != 0) {
            Arrays.fill(table, null);
            size = 0;
            modCount++;
        }
    }

    /** Returns a backed view; AbstractMap builds keySet and values from it. */
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return entries;
    }

    /** Exposes actual entries so setValue updates the map. */
    private final class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        /** Returns the live mapping count. */
        @Override
        public int size() {
            return LearningHashMap.this.size;
        }

        /** Creates a fail-fast iterator that supports its own remove operation. */
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        /** Removes all map entries through this view. */
        @Override
        public void clear() {
            LearningHashMap.this.clear();
        }
    }

    /** Iterates chains and then advances to the next occupied bucket. */
    private final class EntryIterator implements Iterator<Map.Entry<K, V>> {
        private int expectedModCount = modCount;
        private int bucket;
        private Node<K, V> next = nextBucket();
        private Node<K, V> current;

        /** Finds the next nonempty bucket, continuing from the previous scan. */
        private Node<K, V> nextBucket() {
            while (bucket < table.length) {
                final Node<K, V> head = table[bucket++];
                if (head != null) {
                    return head;
                }
            }
            return null;
        }

        /** Reports whether there is another entry; no ordering is promised. */
        @Override
        public boolean hasNext() {
            return next != null;
        }

        /** Returns a live entry or reports exhaustion/structural interference. */
        @Override
        public Map.Entry<K, V> next() {
            checkForModification();
            if (next == null) {
                throw new NoSuchElementException();
            }
            current = next;
            next = current.next == null ? nextBucket() : current.next;
            return current;
        }

        /** Removes the last returned entry and refreshes the expected version. */
        @Override
        public void remove() {
            checkForModification();
            if (current == null) {
                throw new IllegalStateException("Call next before each remove");
            }
            unlink(current.hash, current.key); // cached hash also handles a mutated key
            current = null;
            expectedModCount = modCount;
        }

        /** Detects structural changes; fail-fast is not synchronization. */
        private void checkForModification() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    /** Stores a stable key/hash and a mutable value/chain link. */
    private static final class Node<K, V> implements Map.Entry<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        /** Creates one chain node. */
        private Node(final int hash, final K key, final V value, final Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        /** Returns the key reference. */
        @Override
        public K getKey() {
            return key;
        }

        /** Returns the current value. */
        @Override
        public V getValue() {
            return value;
        }

        /** Replaces the live value without a structural modification. */
        @Override
        public V setValue(final V replacement) {
            final V oldValue = value;
            value = replacement;
            return oldValue;
        }

        /** Uses the Map.Entry key-and-value equality contract. */
        @Override
        public boolean equals(final Object other) {
            if (!(other instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> entry = (Map.Entry<?, ?>) other;
            return Objects.equals(key, entry.getKey()) && Objects.equals(value, entry.getValue());
        }

        /** Uses the Map.Entry hash contract, not the table's spread hash. */
        @Override
        public int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }
    }
}
