
/** Understand the path from a Java source file to a running JVM. @author Snehanjan Chatterjee */
public final class CompileRun {
    /** Computes a doubled value. */
    static int twice(final int value) { return value * 2; }
    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        System.out.println(twice(7));
    }
}
