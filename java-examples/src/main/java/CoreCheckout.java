import java.util.List;
import java.util.Arrays;
import java.util.Objects;

/** A pure quote calculation with explicit units and failure contracts. @author Snehanjan Chatterjee */
public final class CoreCheckout {
    public interface DiscountPolicy {
        /** Returns a side-effect-free discount within zero and subtotal, inclusive. */
        long discountFor(long subtotalCents);
    }
    public static final class Line {
        private final long unitPriceCents;
        private final int quantity;
        /** Establishes nonnegative prices and positive quantities. */
        public Line(final long unitPriceCents, final int quantity) {
            if (unitPriceCents < 0 || quantity <= 0) { throw new IllegalArgumentException("invalid line"); }
            this.unitPriceCents = unitPriceCents;
            this.quantity = quantity;
        }
        /** Calculates the exact line amount in the declared minor unit. */
        private long subtotal() { return Math.multiplyExact(unitPriceCents, quantity); }
    }
    public static final class Quote {
        private final long subtotalCents;
        private final long discountCents;
        /** Stores validated aggregate amounts. */
        private Quote(final long subtotalCents, final long discountCents) {
            this.subtotalCents = subtotalCents;
            this.discountCents = discountCents;
        }
        /** Returns the exact final charge candidate; no external payment is performed. */
        public long totalCents() { return subtotalCents - discountCents; }
        /** Reports the aggregate amount before discount. */
        public long subtotalCents() { return subtotalCents; }
    }
    /** Quotes one currency/minor-unit domain; supplied policies must be side-effect-free. */
    public static Quote quote(final List<Line> lines, final DiscountPolicy policy) {
        Objects.requireNonNull(lines, "lines");
        Objects.requireNonNull(policy, "policy");
        long subtotal = 0;
        for (final Line line : lines) {
            subtotal = Math.addExact(subtotal, Objects.requireNonNull(line, "line").subtotal());
        }
        final long discount = policy.discountFor(subtotal);
        if (discount < 0 || discount > subtotal) { throw new IllegalArgumentException("invalid discount result"); }
        return new Quote(subtotal, discount);
    }
    /** Demonstrates normal quoting and a rejected policy. */
    public static void main(final String[] args) {
        final List<Line> lines = Arrays.asList(new Line(1250, 2), new Line(500, 1));
        System.out.println(quote(lines, subtotal -> 500).totalCents());
        try { quote(lines, subtotal -> subtotal + 1); }
        catch (final IllegalArgumentException expected) { System.out.println("policy rejected"); }
    }
}
