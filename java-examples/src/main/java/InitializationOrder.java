
/** Trace when classes are defined, linked and initialized. @author Snehanjan Chatterjee */
public final class InitializationOrder {
    static final class Holder {
        static final int CONSTANT = 7;
        static { System.out.println("static"); }
        { System.out.println("instance"); }
    }
    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        System.out.println(Holder.CONSTANT);
        new Holder();
    }
}
