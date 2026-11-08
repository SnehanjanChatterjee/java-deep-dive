import java.lang.Math;

/** Separate primitive values, object references and conversion rules. @author Snehanjan Chatterjee */
public final class TypeConversions {

    /** Runs the worked example. */
    public static void main(final String[] args) {
        final int maximum = Integer.MAX_VALUE;
        System.out.println(maximum + 1);
        System.out.println((byte) 130);
        final Integer left = 1000;
        final Integer right = 1000;
        System.out.println(left.equals(right));
    }
}
