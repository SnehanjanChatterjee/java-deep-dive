import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks a named strategy is invoked through its interface contract. @author Snehanjan Chatterjee */
public final class CompositionExampleTest {
    @Test public void testNamedPricingAndAlternativeImplementation() {
        final CompositionExample.Checkout discounted = new CompositionExample.Checkout(new CompositionExample.DiscountPricing());
        assertEquals(90, discounted.total(100));
        final CompositionExample.Pricing regular = new CompositionExample.Pricing() {
            @Override public int apply(final int amount) { return amount; }
        };
        assertEquals(100, new CompositionExample.Checkout(regular).total(100));
    }
    @Test public void testRejectMissingCollaborator() {
        assertThrows(NullPointerException.class, () -> new CompositionExample.Checkout(null));
    }
}
