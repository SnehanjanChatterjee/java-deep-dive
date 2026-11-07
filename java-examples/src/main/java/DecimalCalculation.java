import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/** Calculates one fixed-scale invoice total with an explicit tax rounding boundary. @author Snehanjan Chatterjee */
public final class DecimalCalculation {
    /** Adds tax rounded once per invoice; prices must fit two minor-unit places exactly. */
    public static BigDecimal total(final BigDecimal unitPrice, final int quantity, final BigDecimal taxRate) {
        Objects.requireNonNull(unitPrice, "unitPrice");
        Objects.requireNonNull(taxRate, "taxRate");
        if (unitPrice.signum() < 0 || taxRate.signum() < 0 || quantity <= 0) {
            throw new IllegalArgumentException("nonnegative price/rate and positive quantity required");
        }
        final BigDecimal subtotal = unitPrice.setScale(2, RoundingMode.UNNECESSARY)
                .multiply(BigDecimal.valueOf(quantity));
        final BigDecimal tax = subtotal.multiply(taxRate).setScale(2, RoundingMode.HALF_EVEN);
        return subtotal.add(tax);
    }
    /** Prints an exact subtotal and the declared rounded invoice result. */
    public static void main(final String[] args) {
        final BigDecimal price = new BigDecimal("19.95");
        System.out.println(price.multiply(BigDecimal.valueOf(3)));
        System.out.println(total(price, 3, new BigDecimal("0.0825")));
    }
}
