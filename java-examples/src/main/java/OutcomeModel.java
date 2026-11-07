import java.util.Objects;

/** Use modern type modeling without confusing syntax with immutability. @author Snehanjan Chatterjee */
public final class OutcomeModel {
    enum Status { SUCCESS, FAILURE }
    static final class Outcome {
        final Status status; final String message;
        Outcome(final Status status, final String message) { this.status = Objects.requireNonNull(status); this.message = message; }
    }
    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        final Outcome outcome = new Outcome(Status.SUCCESS, "saved");
        System.out.println(outcome.status + ":" + outcome.message);
    }
}
