import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/** Keeps a list iterator's cursor distinct from its last-returned element. @author Snehanjan Chatterjee */
public final class LinkedListEnds {
    /** Traces backward traversal, replacement, removal and endpoint operations. */
    public static void main(final String[] args) {
        final LinkedList<String> values = new LinkedList<>(Arrays.asList("A", "C"));
        final ListIterator<String> cursor = values.listIterator(1);
        cursor.add("B");
        System.out.println("next/previous indexes=" + cursor.nextIndex() + "/" + cursor.previousIndex());
        System.out.println("previous=" + cursor.previous());
        cursor.set("X");
        System.out.println("next=" + cursor.next());
        cursor.remove();
        System.out.println("after iterator removal=" + values);
        values.addFirst("start"); values.addLast("end");
        System.out.println("removed A=" + values.removeLastOccurrence("A"));
        System.out.println("poll first=" + values.pollFirst() + ", peek last=" + values.peekLast());
        final List<String> descending = new ArrayList<>();
        final Iterator<String> reverse = values.descendingIterator();
        while (reverse.hasNext()) { descending.add(reverse.next()); }
        System.out.println("descending=" + descending);
    }
}
