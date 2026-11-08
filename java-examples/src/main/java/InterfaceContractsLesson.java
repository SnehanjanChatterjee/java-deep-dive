import java.util.Objects;

/** Demonstrates interface method categories and explicit default-method selection. */
public final class InterfaceContractsLesson {
    interface Tagged { }

    @FunctionalInterface
    interface TextOperation {
        String apply(String input);
        boolean equals(Object other);
        /** Supplies behavior without adding an abstract operation. */
        default String kind() { return "operation"; }
        /** Belongs to the interface rather than an implementing object. */
        static String description() { return "one functional operation"; }
    }

    static final class TaggedOperation implements TextOperation, Tagged {
        /** Formats a required input through a named implementation. */
        @Override public String apply(final String input) {
            return "item:" + Objects.requireNonNull(input, "input");
        }
    }

    interface Left {
        /** Defines one default implementation of the shared signature. */
        default String label() { return "left"; }
    }
    interface Right {
        /** Defines a conflicting unrelated default. */
        default String label() { return "right"; }
    }
    static final class Combined implements Left, Right {
        /** Resolves the conflict explicitly using eligible direct interfaces. */
        @Override public String label() { return Left.super.label() + "+" + Right.super.label(); }
    }
    interface Specific extends Left {
        /** Replaces the inherited default with a more specific one. */
        @Override default String label() { return "specific"; }
    }
    static final class SpecificChoice implements Specific { }
    static class NamedBase {
        /** Supplies the concrete class implementation inherited by its subclass. */
        public String label() { return "base"; }
    }
    static final class ClassChoice extends NamedBase implements Left, Right { }

    /** Prints type, functional-operation and three default-selection observations. */
    public static void main(final String[] args) {
        final TextOperation operation = new TaggedOperation();
        System.out.println(operation.apply("java"));
        System.out.println(operation.kind());
        System.out.println(operation instanceof Tagged);
        System.out.println(TextOperation.description());
        System.out.println(new Combined().label());
        System.out.println(new SpecificChoice().label());
        System.out.println(new ClassChoice().label());
    }
}
