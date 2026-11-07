import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
/** Checks the identifier boundary. @author Snehanjan Chatterjee */
public final class ValidatedIdentifierTest {
 @Test public void testPositiveBoundary(){assertEquals(1,ValidatedIdentifier.requirePositive(1));assertEquals(Integer.MAX_VALUE,ValidatedIdentifier.requirePositive(Integer.MAX_VALUE));}
 @Test public void testNonpositiveFailure(){assertThrows(IllegalArgumentException.class,()->ValidatedIdentifier.requirePositive(0));assertThrows(IllegalArgumentException.class,()->ValidatedIdentifier.requirePositive(-1));}
}
