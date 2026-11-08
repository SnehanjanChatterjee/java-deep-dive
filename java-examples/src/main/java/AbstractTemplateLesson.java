import java.util.Objects;

/** Uses an abstract base to share state and an algorithm while subclasses supply one step. */
public final class AbstractTemplateLesson {
    abstract static class Form {
        private final String label;
        /** Establishes the common invariant without calling an overridable method. */
        Form(final String label) { this.label = Objects.requireNonNull(label, "label"); }
        /** Implements the shared operation and delegates only the varying content step. */
        final String format() { return label + ":" + content(); }
        /** Supplies content after the concrete object has completed construction. */
        protected abstract String content();
    }
    static final class TextForm extends Form {
        private final String text;
        /** Initializes inherited state before this class's own text. */
        TextForm(final String label, final String text) {
            super(label);
            this.text = Objects.requireNonNull(text, "text");
        }
        /** Supplies the text representation of this concrete form. */
        @Override protected String content() { return text; }
    }
    static final class CountForm extends Form {
        private final int count;
        /** Rejects a negative count while permitting zero. */
        CountForm(final String label, final int count) {
            super(label);
            if (count < 0) { throw new IllegalArgumentException("negative count"); }
            this.count = count;
        }
        /** Supplies content from a different representation. */
        @Override protected String content() { return Integer.toString(count); }
    }
    /** Invokes one inherited algorithm on two concrete implementations. */
    public static void main(final String[] args) {
        final Form text = new TextForm("note", "hello");
        final Form count = new CountForm("items", 3);
        System.out.println(text.format());
        System.out.println(count.format());
    }
}
