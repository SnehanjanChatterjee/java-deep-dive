import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
/** Checks the subtotal contract. @author Snehanjan Chatterjee */
public final class ContractExampleTest {
 @Test public void testSuccessAndBoundary() {assertEquals(7,ContractExample.subtotal(3,4));assertEquals(0,ContractExample.subtotal(0,0));assertEquals(Integer.MAX_VALUE,ContractExample.subtotal(Integer.MAX_VALUE,0));}
 @Test public void testInvalidAndOverflow() {assertThrows(IllegalArgumentException.class,()->ContractExample.subtotal(-1,0));assertThrows(IllegalArgumentException.class,()->ContractExample.subtotal(0,-1));assertThrows(ArithmeticException.class,()->ContractExample.subtotal(Integer.MAX_VALUE,1));}
}
