import java.util.concurrent.StructuredTaskScope;
/** Groups related tasks under one lifetime; Java 25 preview API. @author Snehanjan Chatterjee */
public final class StructuredConcurrencyDemo {
    /** Joins subtasks before retrieving their successful results. */
    public static void main(final String[] args) throws Exception {
        try (final var scope = StructuredTaskScope.open()) {
            final StructuredTaskScope.Subtask<Integer> first = scope.fork(() -> 42);
            final StructuredTaskScope.Subtask<Integer> second = scope.fork(() -> 1);
            scope.join();
            System.out.println(first.get() + second.get());
        }
    }
}
