import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Objects;

/** Traces an explicitly supplied graph; it does not inspect or force JVM collection. @author Snehanjan Chatterjee */
public final class ReachabilityWalkDemo {
    public static final class Node {
        private final String name;
        private final List<Node> edges = new ArrayList<>();

        /** Creates one conceptual object identity with a display name. */
        public Node(final String name) { this.name = Objects.requireNonNull(name, "name"); }

        /** Adds a strong conceptual edge to a non-null target. */
        public void connect(final Node target) { edges.add(Objects.requireNonNull(target, "target")); }

        /** Removes the first edge identifying the supplied target. */
        public boolean disconnect(final Node target) { return edges.remove(Objects.requireNonNull(target, "target")); }
    }

    /** Lists identities reachable from the supplied model roots in breadth-first discovery order. */
    public static List<String> reachable(final Node... roots) {
        Objects.requireNonNull(roots, "roots");
        final Deque<Node> pending = new ArrayDeque<>();
        final IdentityHashMap<Node, Boolean> visited = new IdentityHashMap<>();
        final List<String> names = new ArrayList<>();
        for (final Node root : roots) { if (root != null && visited.put(root, Boolean.TRUE) == null) { pending.addLast(root); } }
        while (!pending.isEmpty()) {
            final Node node = pending.removeFirst();
            names.add(node.name);
            for (final Node target : node.edges) {
                if (visited.put(target, Boolean.TRUE) == null) { pending.addLast(target); }
            }
        }
        return Collections.unmodifiableList(names);
    }

    /** Distinguishes a removed edge, a retained alias and an unrooted cycle. */
    public static void main(final String[] args) {
        final Node list = new Node("list"), a = new Node("A"), b = new Node("B");
        final Node c = new Node("C"), d = new Node("D");
        list.connect(a); list.connect(b); c.connect(d); d.connect(c);
        System.out.println(reachable(list));
        list.disconnect(a);
        System.out.println(reachable(list, a));
        System.out.println(reachable(list));
        System.out.println(reachable(c));
    }
}
