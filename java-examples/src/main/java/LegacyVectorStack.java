import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.Vector;

/** Explains legacy List inheritance without recommending it as a new stack abstraction. @author Snehanjan Chatterjee */
public final class LegacyVectorStack {
    /** Compares reserved capacity, logical resizing and one-based stack search. */
    public static void main(final String[] args) {
        final Vector<String> vector = new Vector<>(2);
        vector.add("A"); vector.add("B"); vector.add("C");
        System.out.println("vector size=" + vector.size() + ", index 1=" + vector.get(1));
        vector.setSize(5);
        System.out.println("setSize grows=" + vector);
        vector.setSize(2);
        final String[] copied = new String[vector.size()]; vector.copyInto(copied);
        System.out.println("setSize shrinks=" + Arrays.toString(copied));
        final Stack<String> stack = new Stack<>();
        stack.push("A"); stack.push("B"); stack.push("C");
        System.out.println("stack iteration=" + stack);
        System.out.println("peek=" + stack.peek() + ", search B=" + stack.search("B") + ", indexOf B=" + stack.indexOf("B"));
        System.out.println("missing search=" + stack.search("missing"));
        while (!stack.empty()) { System.out.println("pop=" + stack.pop()); }
        try { stack.peek(); throw new IllegalStateException("expected empty-stack failure"); }
        catch (final EmptyStackException expected) { System.out.println("empty peek rejected"); }
    }
}
