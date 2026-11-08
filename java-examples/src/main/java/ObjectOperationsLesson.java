import java.util.Arrays;
import java.util.Objects;

/** Contrasts object inspection, explicit representation and shallow clone ownership. */
public final class ObjectOperationsLesson {
    static final class Stamp implements Cloneable {
        private final String name;
        private final int[] marks;
        /** Copies incoming mutable state while accepting an immutable name. */
        Stamp(final String name, final int[] marks) {
            this.name = Objects.requireNonNull(name, "name");
            this.marks = Objects.requireNonNull(marks, "marks").clone();
        }
        /** Creates a new Stamp whose final array reference still identifies the same array. */
        Stamp shallowCopy() {
            try { return (Stamp) super.clone(); }
            catch (final CloneNotSupportedException impossible) { throw new AssertionError(impossible); }
        }
        /** Constructs a copy through the class's ordinary copying boundary. */
        Stamp independentCopy() { return new Stamp(name, marks); }
        /** Updates one mutable component. */
        void mark(final int index, final int value) { marks[index] = value; }
        /** Returns one component for ownership checks. */
        int markAt(final int index) { return marks[index]; }
        /** Publishes a deliberate readable representation. */
        @Override public String toString() { return name + Arrays.toString(marks); }
    }
    private static final ObjectOperationsLesson INSTANCE = new ObjectOperationsLesson();
    private ObjectOperationsLesson() { }
    /** Returns this runtime class's eagerly initialized shared instance. */
    public static ObjectOperationsLesson instance() { return INSTANCE; }
    /** Shows runtime type, representations and clone aliasing. */
    public static void main(final String[] args) {
        final Stamp original = new Stamp("A", new int[] {1});
        final Stamp shallow = original.shallowCopy();
        final Stamp independent = original.independentCopy();
        System.out.println(original.getClass() == Stamp.class);
        System.out.println(original == shallow);
        shallow.mark(0, 9);
        System.out.println(original);
        System.out.println(independent);
        System.out.println(instance() == instance());
    }
}
