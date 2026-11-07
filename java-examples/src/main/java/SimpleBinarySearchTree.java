import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** An unbalanced ordered map for studying search, traversal and deletion. @author Snehanjan Chatterjee */
public final class SimpleBinarySearchTree<K extends Comparable<? super K>, V> {
    private static final class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> left;
        private Node<K, V> right;
        private Node(final K key, final V value) { this.key = key; this.value = value; }
    }
    private Node<K, V> root;
    private int size;

    /** Returns the number of comparison-distinct keys. */
    public int size() { return size; }
    /** Finds an associated value; containsKey distinguishes a stored null. */
    public V get(final K key) { final Node<K, V> node = find(key); return node == null ? null : node.value; }
    /** Tests presence independently of the stored value. */
    public boolean containsKey(final K key) { return find(key) != null; }
    /** Inserts or replaces a mapping and returns the previous value. */
    public V put(final K key, final V value) {
        Objects.requireNonNull(key, "key");
        if (root == null) { root = new Node<>(key, value); size++; return null; }
        Node<K, V> current = root;
        while (true) {
            final int comparison = key.compareTo(current.key);
            if (comparison == 0) { final V old = current.value; current.value = value; return old; }
            if (comparison < 0) {
                if (current.left == null) { current.left = new Node<>(key, value); break; }
                current = current.left;
            } else {
                if (current.right == null) { current.right = new Node<>(key, value); break; }
                current = current.right;
            }
        }
        size++;
        return null;
    }
    /** Removes one mapping, preserving the search-order invariant. */
    public V remove(final K key) {
        final Node<K, V> node = find(key);
        if (node == null) { return null; }
        final V old = node.value;
        root = delete(root, key);
        size--;
        return old;
    }
    /** Returns an independent container of keys in comparison order. */
    public List<K> keysInOrder() {
        final List<K> keys = new ArrayList<>();
        traverse(root, keys);
        return keys;
    }
    /** Counts nodes on the longest root-to-leaf path; an empty tree has height zero. */
    public int height() { return heightOf(root); }
    /** Locates a key using the same comparison relation as insertion. */
    private Node<K, V> find(final K key) {
        Objects.requireNonNull(key, "key");
        Node<K, V> current = root;
        while (current != null) {
            final int comparison = key.compareTo(current.key);
            if (comparison == 0) { return current; }
            current = comparison < 0 ? current.left : current.right;
        }
        return null;
    }
    /** Replaces a removed two-child node with its in-order successor. */
    private Node<K, V> delete(final Node<K, V> node, final K key) {
        final int comparison = key.compareTo(node.key);
        if (comparison < 0) { node.left = delete(node.left, key); }
        else if (comparison > 0) { node.right = delete(node.right, key); }
        else {
            if (node.left == null) { return node.right; }
            if (node.right == null) { return node.left; }
            Node<K, V> successor = node.right;
            while (successor.left != null) { successor = successor.left; }
            node.key = successor.key;
            node.value = successor.value;
            node.right = delete(node.right, successor.key);
        }
        return node;
    }
    /** Visits the left subtree, node, then right subtree. */
    private void traverse(final Node<K, V> node, final List<K> keys) {
        if (node == null) { return; }
        traverse(node.left, keys);
        keys.add(node.key);
        traverse(node.right, keys);
    }
    /** Computes height without claiming that insertion balances the tree. */
    private int heightOf(final Node<K, V> node) {
        return node == null ? 0 : 1 + Math.max(heightOf(node.left), heightOf(node.right));
    }
    /** Traces a two-child root deletion and an intentionally degenerate tree. */
    public static void main(final String[] args) {
        final SimpleBinarySearchTree<Integer, String> tree = new SimpleBinarySearchTree<>();
        for (final int key : new int[] {20, 10, 30, 25, 40}) { tree.put(key, "v" + key); }
        System.out.println(tree.get(25));
        System.out.println(tree.remove(20));
        System.out.println(tree.keysInOrder());
        final SimpleBinarySearchTree<Integer, String> chain = new SimpleBinarySearchTree<>();
        for (final int key : new int[] {10, 20, 30, 40}) { chain.put(key, "v" + key); }
        System.out.println("chain height=" + chain.height());
    }
}
