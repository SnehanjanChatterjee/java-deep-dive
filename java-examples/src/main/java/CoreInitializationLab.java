/** Separates loading, constant use and successful/failed initialization. @author Snehanjan Chatterjee */
public final class CoreInitializationLab {
    private static final class Holder {
        private static final int CONSTANT = 7;
        private static final int VALUE = initialize();
        /** Supplies a nonconstant static value during initialization. */
        private static int initialize() { System.out.println("initialized"); return 42; }
    }
    private static final class Broken {
        private static final int VALUE = fail();
        /** Produces a controlled initialization failure. */
        private static int fail() { throw new IllegalStateException("initialization failed"); }
    }
    /** Shows the first and later active uses of an erroneous class separately. */
    public static void main(final String[] args) throws ClassNotFoundException {
        System.out.println(Holder.CONSTANT);
        Class.forName(Holder.class.getName(), false, CoreInitializationLab.class.getClassLoader());
        System.out.println("loaded without initialization");
        System.out.println(Holder.VALUE);
        try { System.out.println(Broken.VALUE); }
        catch (final ExceptionInInitializerError expected) { System.out.println("first initialization failure"); }
        try { System.out.println(Broken.VALUE); }
        catch (final NoClassDefFoundError expected) { System.out.println("later erroneous class"); }
    }
}
