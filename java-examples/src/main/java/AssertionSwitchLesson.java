/** Deliberately supplies an invalid internal value to expose assertion enablement. */
public final class AssertionSwitchLesson {
    /** Prints only when the deliberately failing assertion is disabled. */
    public static void main(final String[] args) {
        final int available = -1;
        assert available >= 0 : "negative internal count";
        System.out.println("continued");
    }
}
