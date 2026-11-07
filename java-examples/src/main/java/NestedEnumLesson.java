/** Distinguishes fixed named values from nested object relationships. @author Snehanjan Chatterjee */
public final class NestedEnumLesson {
    enum State {
        NEW, SENT;
        /** Allows sending only while the delivery is new. */
        boolean canSend() { return this == NEW; }
    }
    static final class Label {
        private final String text;
        /** Creates a label without needing an enclosing delivery. */
        Label(final String text) { this.text = text; }
    }
    private final String destination;
    /** Creates the enclosing object used by an inner receipt. */
    private NestedEnumLesson(final String destination) { this.destination = destination; }
    final class Receipt {
        /** Reads state from the particular enclosing object. */
        String destination() { return NestedEnumLesson.this.destination; }
    }
    /** Shows enum identity, static nesting and an inner object's owner. */
    public static void main(final String[] args) {
        final Label label = new Label("parcel");
        final NestedEnumLesson delivery = new NestedEnumLesson("West");
        final Receipt receipt = delivery.new Receipt();
        System.out.println(State.NEW.canSend());
        System.out.println(label.text);
        System.out.println(receipt.destination());
    }
}
