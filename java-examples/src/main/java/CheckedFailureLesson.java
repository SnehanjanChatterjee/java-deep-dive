/** Makes a checked failure obligation and a preserved cause observable. @author Snehanjan Chatterjee */
public final class CheckedFailureLesson {
    /** Describes an invalid count representation at this API boundary. */
    public static final class CountFormatException extends Exception {
        private static final long serialVersionUID = 1L;
        /** Keeps the low-level parsing or range failure. */
        CountFormatException(final Throwable cause) { super("Invalid count", cause); }
    }
    /** Parses a nonnegative count or declares a checked format failure. */
    public static int readCount(final String text) throws CountFormatException {
        try {
            final int count = Integer.parseInt(text);
            if (count < 0) { throw new IllegalArgumentException("negative count"); }
            return count;
        } catch (final IllegalArgumentException cause) {
            throw new CountFormatException(cause);
        }
    }
    /** Handles the checked failure at the boundary that can report input trouble. */
    public static void main(final String[] args) {
        try {
            System.out.println(readCount("12"));
            System.out.println(readCount("many"));
        } catch (final CountFormatException failure) {
            System.out.println(failure.getMessage());
            System.out.println(failure.getCause().getClass().getSimpleName());
        } finally {
            System.out.println("attempt finished");
        }
    }
}
