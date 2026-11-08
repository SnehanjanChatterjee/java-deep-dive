import java.util.Objects;

/** Demonstrates local classes, anonymous classes and captured values without lambdas. */
public final class LocalAnonymousLesson {
    interface Message { String text(); }
    private final String name;
    /** Stores the enclosing object's name. */
    public LocalAnonymousLesson(final String name) { this.name = Objects.requireNonNull(name, "name"); }
    /** Creates a named local implementation capturing a method parameter. */
    public Message local(final String prefix) {
        Objects.requireNonNull(prefix, "prefix");
        class LocalMessage implements Message {
            /** Reads a captured parameter and the enclosing object's field. */
            @Override public String text() { return prefix + LocalAnonymousLesson.this.name; }
        }
        return new LocalMessage();
    }
    /** Creates an unnamed implementation with its own field and this reference. */
    public Message anonymous(final String prefix) {
        Objects.requireNonNull(prefix, "prefix");
        return new Message() {
            private final String name = "inner";
            /** Distinguishes the anonymous object from its enclosing object. */
            @Override public String text() {
                return prefix + LocalAnonymousLesson.this.name + ":" + this.name;
            }
        };
    }
    /** Uses the returned objects after their factory calls complete. */
    public static void main(final String[] args) {
        final LocalAnonymousLesson outer = new LocalAnonymousLesson("Ada");
        System.out.println(outer.local("Hello ").text());
        System.out.println(outer.anonymous("Hello ").text());
    }
}
