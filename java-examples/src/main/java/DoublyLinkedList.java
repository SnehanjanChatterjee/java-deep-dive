import java.util.NoSuchElementException;

/** A focused generic linked-list implementation with explicit endpoint invariants. @author Snehanjan Chatterjee */
public final class DoublyLinkedList<T> {
    private static final class Node<T> {
        private final T value;
        private Node<T> previous;
        private Node<T> next;
        /** Creates one detached node. */
        private Node(final T value) { this.value = value; }
    }
    private Node<T> first;
    private Node<T> last;
    private int size;

    /** Reports logical elements. */
    public int size() { return size; }

    /** Appends to the last endpoint. */
    public void addLast(final T value) {
        final Node<T> added = new Node<>(value);
        added.previous = last;
        if (last == null) { first = added; } else { last.next = added; }
        last = added;
        size++;
    }

    /** Inserts at a logical position, allowing the position after the last element. */
    public void add(final int index, final T value) {
        if (index < 0 || index > size) { throw new IndexOutOfBoundsException("index=" + index); }
        if (index == size) { addLast(value); return; }
        final Node<T> successor = nodeAt(index);
        final Node<T> added = new Node<>(value);
        added.next = successor;
        added.previous = successor.previous;
        if (successor.previous == null) { first = added; } else { successor.previous.next = added; }
        successor.previous = added;
        size++;
    }

    /** Reads a value after finding its nearest-end traversal path. */
    public T get(final int index) { return nodeAt(index).value; }

    /** Removes the first logical element. */
    public T removeFirst() {
        if (first == null) { throw new NoSuchElementException("empty list"); }
        return unlink(first);
    }

    /** Removes a logical index. */
    public T remove(final int index) { return unlink(nodeAt(index)); }

    /** Locates a node from the nearer endpoint without changing links. */
    private Node<T> nodeAt(final int index) {
        if (index < 0 || index >= size) { throw new IndexOutOfBoundsException("index=" + index); }
        if (index < size / 2) {
            Node<T> node = first;
            for (int position = 0; position < index; position++) { node = node.next; }
            return node;
        }
        Node<T> node = last;
        for (int position = size - 1; position > index; position--) { node = node.previous; }
        return node;
    }

    /** Repairs both directions and endpoints, then detaches the removed node. */
    private T unlink(final Node<T> node) {
        if (node.previous == null) { first = node.next; } else { node.previous.next = node.next; }
        if (node.next == null) { last = node.previous; } else { node.next.previous = node.previous; }
        node.previous = null;
        node.next = null;
        size--;
        return node.value;
    }

    /** Demonstrates middle insertion, removal and singleton endpoint repair. */
    public static void main(final String[] args) {
        final DoublyLinkedList<String> list = new DoublyLinkedList<>();
        list.addLast("A"); list.addLast("C"); list.add(1, "B");
        System.out.println(list.remove(1));
        System.out.println(list.get(1));
        list.removeFirst(); list.removeFirst();
        list.addLast("reused");
        System.out.println(list.get(0));
    }
}
