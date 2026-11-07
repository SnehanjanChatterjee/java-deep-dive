import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/** Tests conservation and failure atomicity under globally ordered locks. @author Snehanjan Chatterjee */
public final class OrderedTransferLabTest {
    @Test public void testSuccessInsufficientAndSameAccount() {
        final OrderedTransferLab.Account left = new OrderedTransferLab.Account(10); final OrderedTransferLab.Account right = new OrderedTransferLab.Account(2);
        assertTrue(OrderedTransferLab.transfer(left, right, 3)); assertEquals(7, left.balance()); assertEquals(5, right.balance());
        assertFalse(OrderedTransferLab.transfer(left, right, 8)); assertTrue(OrderedTransferLab.transfer(left, left, 1)); assertEquals(7, left.balance());
        assertThrows(IllegalArgumentException.class, () -> OrderedTransferLab.transfer(left, right, 0));
        assertThrows(NullPointerException.class, () -> OrderedTransferLab.transfer(null, right, 1));
        assertThrows(NullPointerException.class, () -> OrderedTransferLab.transfer(left, null, 1));
        assertThrows(IllegalArgumentException.class, () -> new OrderedTransferLab.Account(-1));
    }
    @Test public void testOverflowDoesNotDebitSource() {
        final OrderedTransferLab.Account from = new OrderedTransferLab.Account(1); final OrderedTransferLab.Account to = new OrderedTransferLab.Account(Integer.MAX_VALUE);
        assertThrows(ArithmeticException.class, () -> OrderedTransferLab.transfer(from, to, 1)); assertEquals(1, from.balance()); assertEquals(Integer.MAX_VALUE, to.balance());
    }
    @Test public void testOpposingTransfersTerminateAndConserve() throws InterruptedException {
        final OrderedTransferLab.Account left = new OrderedTransferLab.Account(100); final OrderedTransferLab.Account right = new OrderedTransferLab.Account(100);
        final Thread a = new Thread(() -> { for (int i = 0; i < 1000; i++) { OrderedTransferLab.transfer(left, right, 1); } });
        final Thread b = new Thread(() -> { for (int i = 0; i < 1000; i++) { OrderedTransferLab.transfer(right, left, 1); } });
        a.setDaemon(true); b.setDaemon(true); a.start(); b.start(); a.join(5000); b.join(5000);
        assertFalse(a.isAlive()); assertFalse(b.isAlive()); assertEquals(200, left.balance() + right.balance());
    }
}
