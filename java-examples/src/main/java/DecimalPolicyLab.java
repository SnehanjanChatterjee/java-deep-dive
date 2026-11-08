import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/** Compares decimal operations and explicitly chosen rounding boundaries. @author Snehanjan Chatterjee */
public final class DecimalPolicyLab {
    /** Prints exact arithmetic, significant-digit rounding and negative tie behavior. */
    public static void main(final String[] args) {
        final BigDecimal a = new BigDecimal("1.20"), b = new BigDecimal("3.4");
        System.out.println("add=" + a.add(b).toPlainString());
        System.out.println("subtract=" + a.subtract(b).toPlainString());
        System.out.println("multiply=" + a.multiply(b).toPlainString());
        System.out.println("exact divide=" + BigDecimal.ONE.divide(new BigDecimal("8")).toPlainString());
        System.out.println("rounded divide=" + BigDecimal.ONE.divide(new BigDecimal("3"), 4, RoundingMode.HALF_UP));
        System.out.println("precision4=" + new BigDecimal("12345.67").round(new MathContext(4, RoundingMode.HALF_EVEN)).toPlainString());
        final BigDecimal dividend = new BigDecimal("7.25"), divisor = new BigDecimal("2");
        System.out.println("quotient=" + dividend.divideToIntegralValue(divisor) + ", remainder=" + dividend.remainder(divisor));
        for (final RoundingMode mode : RoundingMode.values()) {
            try { System.out.println(mode + ": " + new BigDecimal("-1.25").setScale(1, mode)); }
            catch (final ArithmeticException unnecessary) { System.out.println(mode + ": rounding rejected"); }
        }
        final BigDecimal price = new BigDecimal("19.95"), quantity = new BigDecimal("3"), rate = new BigDecimal("0.0825");
        final BigDecimal perItem = price.multiply(rate).setScale(2, RoundingMode.HALF_EVEN).multiply(quantity);
        final BigDecimal perInvoice = price.multiply(quantity).multiply(rate).setScale(2, RoundingMode.HALF_EVEN);
        System.out.println("item tax=" + perItem + ", invoice tax=" + perInvoice);
        System.out.println("normalized scale=" + new BigDecimal("1000").stripTrailingZeros().scale());
    }
}
