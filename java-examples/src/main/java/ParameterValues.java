
/** Trace parameter values, object mutation and method selection. @author Snehanjan Chatterjee */
public final class ParameterValues {
    static final class User { String name; User(final String name) { this.name = name; } }
    static void rename(final User user) { user.name = "Mira"; }
    static void replace(User user) { user = new User("Leo"); }
    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        final User user = new User("Ava");
        rename(user);
        replace(user);
        System.out.println(user.name);
    }
}
