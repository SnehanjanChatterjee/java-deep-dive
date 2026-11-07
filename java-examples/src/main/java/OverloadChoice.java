/** Exhibits the fixed-arity, boxing and variable-arity applicability phases. @author Snehanjan Chatterjee */
public final class OverloadChoice {
    /** Accepts a widening primitive conversion. */
    static String pick(final long value) { return "long"; }
    /** Accepts an existing wrapper without unboxing. */
    static String pick(final Integer value) { return "Integer"; }
    /** Accepts an array or a variable-length argument sequence. */
    static String pick(final int... values) { return "varargs"; }
    /** Prints the compiler-selected overload for each call. */
    public static void main(final String[] args) {
        System.out.println(pick(1));
        System.out.println(pick(Integer.valueOf(1)));
        System.out.println(pick());
    }
}
