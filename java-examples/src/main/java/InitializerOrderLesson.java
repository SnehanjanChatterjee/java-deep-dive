/** Traces class initialization separately from each object's initialization. */
public final class InitializerOrderLesson {
    static final class Ticket {
        private static final int FIRST = trace("static field", 1);
        static { System.out.println("static block " + FIRST); }
        static { System.out.println("second static block"); }
        private int value = trace("instance field", 2);
        private int initializerRuns;
        { initializerRuns++; System.out.println("instance block " + value); }

        /** Delegates without repeating the object's initializer sequence. */
        Ticket() { this(7); System.out.println("no-argument body"); }

        /** Completes the object's chosen initial state. */
        Ticket(final int value) { this.value = value; System.out.println("argument body " + value); }

        /** Reports how often this object's initializer ran. */
        int initializerRuns() { return initializerRuns; }

        /** Returns the constructed value. */
        int value() { return value; }
    }

    /** Prints an initialization phase and supplies its field value. */
    private static int trace(final String phase, final int value) {
        System.out.println(phase);
        return value;
    }

    /** Creates two objects from the same initialized class. */
    public static void main(final String[] args) {
        System.out.println("before first new");
        final Ticket first = new Ticket();
        System.out.println("first=" + first.value());
        final Ticket second = new Ticket(9);
        System.out.println("second=" + second.value());
    }
}
