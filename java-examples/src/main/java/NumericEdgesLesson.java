/** Makes arithmetic promotion and floating-point special values observable. */
public final class NumericEdgesLesson {
    /** Prints values whose operand types determine their meaning. */
    public static void main(final String[] args) {
        System.out.println('A' + 1);
        System.out.println((char) ('A' + 1));
        System.out.println((int) 16_777_217F);
        System.out.println(1.0 / 0.0);
        System.out.println(1.0 / -0.0);
        System.out.println(Double.isNaN(0.0 / 0.0));
        System.out.println(0.0 == -0.0);
        System.out.println(Double.valueOf(0.0).equals(Double.valueOf(-0.0)));
    }
}
