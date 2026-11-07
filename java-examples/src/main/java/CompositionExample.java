import java.util.Objects;

/** Design behavior with interfaces and distinguish dispatch from hiding. @author Snehanjan Chatterjee */
public final class CompositionExample {
    interface Pricing { int apply(int amount); }
    static final class Checkout {
        private final Pricing pricing;
        Checkout(final Pricing pricing) { this.pricing = Objects.requireNonNull(pricing); }
        int total(final int amount) { return pricing.apply(amount); }
    }
    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        final Pricing pricing = amount -> amount - 10;
        final Checkout checkout = new Checkout(pricing);
        System.out.println(checkout.total(100));
    }
}
