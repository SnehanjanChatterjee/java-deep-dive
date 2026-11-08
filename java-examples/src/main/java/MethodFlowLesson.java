/** Shows that return exits one invocation while its caller can continue looping. */
public final class MethodFlowLesson {
    /** Prints only positive values; an early return has no result expression. */
    private static void printPositive(final int number) {
        if (number <= 0) { return; }
        System.out.println(number);
    }
    /** Calls the helper three times and then continues. */
    public static void main(final String[] args) {
        for (int number = -1; number <= 1; number++) { printPositive(number); }
        System.out.println("caller continues");
    }
}
