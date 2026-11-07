import java.util.Objects;

/** Separates invocation-local values from objects reached through references. */
public final class JvmStackHeapLesson {
    public static final class Box {
        private int value;
        /** Stores the primitive field belonging to this object. */
        public Box(final int value) { this.value = value; }
        /** Returns this particular object's field. */
        public int value() { return value; }
    }

    /** Changes a copied primitive parameter and writes its result through a copied reference. */
    public static void change(final Box argument, int copiedNumber) {
        Objects.requireNonNull(argument, "argument");
        copiedNumber++;
        argument.value = copiedNumber;
    }

    /** Returns a new object whose lifetime can extend beyond this invocation. */
    public static Box createBox(final int value) { return new Box(value); }

    /** Makes aliasing, primitive parameter copies and a returned object observable. */
    public static void main(final String[] args) {
        final int number = 7;
        final Box original = new Box(number);
        final Box alias = original;
        change(original, number);
        System.out.println(number);
        System.out.println(alias.value());
        final Box created = createBox(4);
        System.out.println(created.value());
        System.out.println(original == alias);
    }
}
