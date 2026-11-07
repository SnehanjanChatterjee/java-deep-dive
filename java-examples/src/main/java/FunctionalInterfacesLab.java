/** Connects a named interface method, a named implementation and lambda syntax. @author Snehanjan Chatterjee */
public final class FunctionalInterfacesLab {
    @FunctionalInterface
    public interface TextRule {
        boolean accepts(String text);
        /** Reuses the abstract method; this default method is not a second requirement. */
        default boolean rejects(final String text) { return !accepts(text); }
    }
    public static final class ThreeLetters implements TextRule {
        /** Accepts non-null text containing exactly three UTF-16 code units. */
        @Override public boolean accepts(final String text) { return text.length() == 3; }
    }
    /** Invokes each implementation through the same interface method. */
    public static void main(final String[] args) {
        final TextRule named = new ThreeLetters();
        final TextRule explicit = (String text) -> { return text.length() == 3; };
        final TextRule concise = text -> text.length() == 3;
        System.out.println(named.accepts("cat"));
        System.out.println(explicit.accepts("dog"));
        System.out.println(concise.rejects("Java"));
    }
}
