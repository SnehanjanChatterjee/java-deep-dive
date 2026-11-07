import java.math.BigDecimal;
import java.math.RoundingMode;

/** Demonstrates decimal rounding and representation-sensitive equality. @author Snehanjan Chatterjee */
public final class BigDecimalExample {
    /** Compares arithmetic, equality and exact narrowing policies. */
    public static void main(final String[] args) {
        final BigDecimal first = new BigDecimal("1.0");
        final BigDecimal second = new BigDecimal("1.00");
        System.out.println(first.equals(second));
        System.out.println(first.compareTo(second));
        System.out.println(BigDecimal.ONE.divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP));
        System.out.println(new BigDecimal("1.25").movePointRight(2).intValueExact());
        try { new BigDecimal("1.25").intValueExact(); }
        catch (final ArithmeticException expected) { System.out.println("fraction rejected"); }
    }
}
