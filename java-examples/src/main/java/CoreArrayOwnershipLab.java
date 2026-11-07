import java.util.Arrays;

/** Demonstrates array reification, shallow copies and varargs shape. @author Snehanjan Chatterjee */
public final class CoreArrayOwnershipLab {
    /** Runs independent, deterministic ownership examples. */
    public static void main(final String[] args) {
        final int[][] original = {{1, 2}, {3, 4}};
        final int[][] shallow = original.clone();
        shallow[0][0] = 9;
        System.out.println(original[0][0]);
        System.out.println(original == shallow);
        System.out.println(original[0] == shallow[0]);
        System.out.println(Arrays.asList(new int[] {1, 2, 3}).size());
        final Object[] objects = new String[] {"safe"};
        try { objects[0] = Integer.valueOf(7); }
        catch (final ArrayStoreException expected) { System.out.println("component rejected"); }
    }
}
