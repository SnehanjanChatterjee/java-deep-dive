import java.util.Iterator;
import java.util.NoSuchElementException;

/** Implements reusable Iterable values with independently owned traversal state. @author Snehanjan Chatterjee */
public final class IntRange implements Iterable<Integer> {
    private final int start;
    private final int endExclusive;
    /** Defines an ascending half-open range, including an empty start-equals-end range. */
    public IntRange(final int start, final int endExclusive) {
        if (start > endExclusive) { throw new IllegalArgumentException("start after end"); }
        this.start = start;
        this.endExclusive = endExclusive;
    }
    /** Supplies a new cursor for every traversal. */
    @Override public Iterator<Integer> iterator() { return new Cursor(start, endExclusive); }
    private static final class Cursor implements Iterator<Integer> {
        private int next;
        private final int endExclusive;
        /** Captures immutable bounds and this traversal's changing next position. */
        private Cursor(final int start, final int endExclusive) { next = start; this.endExclusive = endExclusive; }
        /** Observes availability without advancing. */
        @Override public boolean hasNext() { return next < endExclusive; }
        /** Returns one value and advances, rejecting exhausted traversal. */
        @Override public Integer next() {
            if (!hasNext()) { throw new NoSuchElementException("range exhausted"); }
            return next++;
        }
    }
    /** Contrasts enhanced-for traversal with two independent explicit cursors. */
    public static void main(final String[] args) {
        final IntRange range = new IntRange(2, 5);
        for (final int value : range) { System.out.println(value); }
        final Iterator<Integer> left = range.iterator();
        final Iterator<Integer> right = range.iterator();
        System.out.println("independent=" + left.next() + "/" + left.next() + "/" + right.next());
    }
}
