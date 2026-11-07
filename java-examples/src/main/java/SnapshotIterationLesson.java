import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/** Makes container snapshot and mutable-element semantics visible. @author Snehanjan Chatterjee */
public final class SnapshotIterationLesson {
    /** Retains one iterator across replacement and structural mutation. */
    public static void main(final String[] args) {
        final CopyOnWriteArrayList<String> values = new CopyOnWriteArrayList<>(Arrays.asList("A", "B"));
        final Iterator<String> before = values.iterator();
        values.set(0, "changed"); values.add("C");
        final List<String> observed = new ArrayList<>();
        before.forEachRemaining(observed::add);
        System.out.println(observed); System.out.println(values);
        final StringBuilder mutable = new StringBuilder("old");
        final CopyOnWriteArrayList<StringBuilder> objects = new CopyOnWriteArrayList<>();
        objects.add(mutable);
        final Iterator<StringBuilder> snapshot = objects.iterator();
        mutable.append("-new");
        System.out.println(snapshot.next());
    }
}
