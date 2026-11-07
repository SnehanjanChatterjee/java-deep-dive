import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

/** Checks both array ownership boundaries and matching value contracts. @author Snehanjan Chatterjee */
public final class ImmutableRouteTest {
    @Test public void testInputAndOutputCopies() {
        final int[] input = {2, 4};
        final ImmutableRoute route = new ImmutableRoute(input);
        input[0] = 9; route.stops()[0] = 8;
        assertArrayEquals(new int[] {2, 4}, route.stops());
        assertArrayEquals(new int[0], new ImmutableRoute(new int[0]).stops());
    }
    @Test public void testValueEqualityAndNull() {
        final ImmutableRoute route = new ImmutableRoute(new int[] {2, 4});
        final ImmutableRoute same = new ImmutableRoute(new int[] {2, 4});
        assertEquals(route, route); assertEquals(route, same); assertEquals(route.hashCode(), same.hashCode());
        assertFalse(route.equals(new ImmutableRoute(new int[] {2, 5})));
        assertFalse(route.equals(null)); assertFalse(route.equals("other"));
        assertThrows(NullPointerException.class, () -> new ImmutableRoute(null));
    }
}
