import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/** Separates shared mutation, local rebinding and explicit replacement. @author Snehanjan Chatterjee */
public final class ParameterValuesTest {
    @Test public void testMutationAndLocalReassignment() {
        final ParameterValues.User user = new ParameterValues.User("Ava");
        ParameterValues.rename(user); ParameterValues.replace(user);
        assertEquals("Mira", user.name);
    }
    @Test public void testReturnedReplacementIsDistinct() {
        final ParameterValues.User original = new ParameterValues.User("Ava");
        final ParameterValues.User replacement = ParameterValues.replacement();
        assertNotSame(original, replacement); assertEquals("Leo", replacement.name);
        assertEquals("Ava", original.name);
    }
}
