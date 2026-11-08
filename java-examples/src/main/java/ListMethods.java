import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Connects list construction, bulk insertion, positions and typed arrays. @author Snehanjan Chatterjee */
public final class ListMethods {
    /** Uses only specified List results rather than private array-capacity observations. */
    public static void main(final String[] args) {
        final ArrayList<String> names = new ArrayList<>(8);
        System.out.println("initial size=" + names.size());
        names.addAll(Arrays.asList("A", "C", "A"));
        names.addAll(1, Arrays.asList("B", "B"));
        System.out.println(names);
        System.out.println("first/last A=" + names.indexOf("A") + "/" + names.lastIndexOf("A"));
        System.out.println("set old=" + names.set(2, "D"));
        System.out.println("remove value=" + names.remove("B"));
        final String[] snapshot = names.toArray(new String[0]);
        names.set(0, "changed");
        System.out.println("array snapshot=" + Arrays.toString(snapshot));
        final List<String> range = names.subList(1, 3);
        range.clear();
        System.out.println("range clear=" + names);
        names.ensureCapacity(20);
        names.trimToSize();
        System.out.println("logical size=" + names.size());
    }
}
