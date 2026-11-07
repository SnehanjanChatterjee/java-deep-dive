/** Shows shallow array copying and runtime component checks. @author Snehanjan Chatterjee */
public final class ArrayOwnership {
    /** Detaches all primitive rows while preserving null and ragged rows. */
    public static int[][] copyRows(final int[][] source) {
        final int[][] result = source.clone();
        for (int row = 0; row < result.length; row++) {
            if (result[row] != null) {
                result[row] = result[row].clone();
            }
        }
        return result;
    }
    /** Traces outer references, shared rows and a covariant store. */
    public static void main(final String[] args) {
        final int[][] original = {{1, 2}, {3, 4}};
        final int[][] shallow = original.clone();
        shallow[0][0] = 9;
        System.out.println(original[0][0]);
        System.out.println(original == shallow);
        System.out.println(original[0] == shallow[0]);
        final Object[] objects = new String[] {"safe"};
        try {
            objects[0] = Integer.valueOf(7);
        } catch (final ArrayStoreException failure) {
            System.out.println("component rejected");
        }
    }
}
