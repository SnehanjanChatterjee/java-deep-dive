import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/** Derives a composed invocation type from one target and one return filter. @author Snehanjan Chatterjee */
public final class MethodHandleComposition {
    /** Doubles an int and rejects arithmetic overflow. */
    private static int twice(final int value) { return Math.multiplyExact(value, 2); }

    /** Composes int arithmetic with int-to-text conversion, then invokes the result exactly. */
    public static void main(final String[] args) throws Throwable {
        final MethodHandles.Lookup lookup = MethodHandles.lookup();
        final MethodHandle arithmetic = lookup.findStatic(MethodHandleComposition.class, "twice",
            MethodType.methodType(int.class, int.class));
        final MethodHandle text = lookup.findStatic(String.class, "valueOf",
            MethodType.methodType(String.class, int.class));
        final MethodHandle composed = MethodHandles.filterReturnValue(arithmetic, text);
        System.out.println("type=" + composed.type());
        System.out.println("value=" + (String) composed.invokeExact(3));
    }
}
