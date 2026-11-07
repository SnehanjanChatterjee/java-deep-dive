import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.WrongMethodTypeException;

/** Makes exact invocation and explicit type adaptation observable. @author Snehanjan Chatterjee */
public final class MethodHandleLesson {
    /** Supplies a small statically typed target. */
    private static int twice(final int value) { return Math.multiplyExact(value, 2); }

    /** Looks up a target, invokes it exactly, adapts it and rejects a mismatched call. */
    public static void main(final String[] args) throws Throwable {
        final MethodHandle target = MethodHandles.lookup().findStatic(MethodHandleLesson.class, "twice", MethodType.methodType(int.class, int.class));
        System.out.println((int) target.invokeExact(3));
        final MethodHandle adapted = target.asType(MethodType.methodType(long.class, short.class));
        System.out.println((long) adapted.invokeExact((short) 4));
        try { final int ignored = (int) target.invokeExact(4L); throw new IllegalStateException("unexpected result " + ignored); }
        catch (final WrongMethodTypeException expected) { System.out.println("wrong signature"); }
    }
}
