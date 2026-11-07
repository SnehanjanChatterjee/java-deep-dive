/** Makes invocation, return values and recursive progress explicit. @author Snehanjan Chatterjee */
public final class MethodsRecursion {
    /** Returns a doubled integer, detecting overflow. */
    public static int twice(final int value) {
        return Math.multiplyExact(value, 2);
    }

    /** Returns n factorial for the long-representable domain 0 through 20. */
    public static long factorial(final int number) {
        if (number < 0 || number > 20) {
            throw new IllegalArgumentException("number must be between 0 and 20");
        }
        if (number == 0) {
            return 1L;
        }
        return number * factorial(number - 1);
    }

    /** Implements the same factorial contract with constant auxiliary storage. */
    public static long factorialIterative(final int number) {
        if (number < 0 || number > 20) {
            throw new IllegalArgumentException("number must be between 0 and 20");
        }
        long product = 1L;
        for (int factor = 2; factor <= number; factor++) {
            product *= factor;
        }
        return product;
    }

    /** Shows how a return value becomes an argument in the caller. */
    public static void main(final String[] args) {
        final int result = twice(7);
        System.out.println(result);
        System.out.println(factorial(4));
    }
}
