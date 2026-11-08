/** Records normal, handled and finally completion without discarding pending failures. */
public final class ExceptionPathsLesson {
    /** Returns the path taken through one try/catch/finally statement. */
    public static String path(final boolean fail) {
        final StringBuilder events = new StringBuilder("try");
        try {
            if (fail) { throw new IllegalArgumentException("invalid"); }
            events.append(" body-end");
        } catch (final IllegalArgumentException failure) {
            events.append(" catch");
        } finally {
            events.append(" finally");
        }
        return events.append(" after").toString();
    }
    /** Returns the value evaluated before finally changes the local variable. */
    public static int returnBeforeFinally() {
        int value = 1;
        try { return value; }
        finally { value = 2; }
    }
    /** Shows both completion paths and the pending return value. */
    public static void main(final String[] args) {
        System.out.println(path(false));
        System.out.println(path(true));
        System.out.println(returnBeforeFinally());
    }
}
