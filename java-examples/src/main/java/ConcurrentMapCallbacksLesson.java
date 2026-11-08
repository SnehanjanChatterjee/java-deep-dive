import java.util.concurrent.ConcurrentHashMap;

/** Shows why computeIfAbsent does not mean one callback forever per key. @author Snehanjan Chatterjee */
public final class ConcurrentMapCallbacksLesson {
    /** Runs sequential callback outcomes to isolate absence, failure and successful installation. */
    public static String[] observations() {
        final ConcurrentHashMap<String, Integer> scores = new ConcurrentHashMap<>();
        final int[] calls = {0};
        scores.computeIfAbsent("java", key -> {
            calls[0]++;
            return null;
        });
        final boolean absentAfterNull = !scores.containsKey("java");
        String failedMessage = "missing failure";
        try {
            scores.computeIfAbsent("java", key -> {
                calls[0]++;
                throw new IllegalArgumentException("calculation failed");
            });
            throw new AssertionError("calculation should fail");
        } catch (final IllegalArgumentException expected) {
            failedMessage = expected.getMessage();
        }
        final boolean absentAfterFailure = !scores.containsKey("java");
        final int installed = scores.computeIfAbsent("java", key -> {
            calls[0]++;
            return 42;
        });
        final int existing = scores.computeIfAbsent("java", key -> {
            throw new AssertionError("present mapping must not need this computation");
        });
        return new String[] {"absent after null=" + absentAfterNull,
            "failure=" + failedMessage, "absent after failure=" + absentAfterFailure, "installed=" + installed,
            "existing=" + existing, "callback calls=" + calls[0]};
    }

    /** The callback counter is confined to this sequential experiment, not shared concurrent state. */
    public static void main(final String[] args) {
        for (final String observation : observations()) {
            System.out.println(observation);
        }
    }
}
