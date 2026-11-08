import java.util.NoSuchElementException;

/** Distinguishes an unbound scoped key from a key deliberately bound to null. @author Snehanjan Chatterjee */
public final class ScopedAccessLesson {
    /** Records unbound access policy and restoration around a present-null binding. */
    public static String[] observations() {
        final ScopedValue<String> context = ScopedValue.newInstance();
        final String[] answers = new String[5];
        answers[0] = "unbound fallback=" + context.orElse("guest");
        try {
            context.get();
            throw new AssertionError("unbound get must fail");
        } catch (final NoSuchElementException expected) {
            answers[1] = "unbound get=NoSuchElementException";
        }
        ScopedValue.where(context, null).run(() -> {
            answers[2] = "inside bound=" + context.isBound();
            answers[3] = "inside fallback=" + context.orElse("guest");
        });
        answers[4] = "after bound=" + context.isBound();
        return answers;
    }

    /** Requires Java 25 without preview flags. */
    public static void main(final String[] args) {
        for (final String answer : observations()) {
            System.out.println(answer);
        }
    }
}
