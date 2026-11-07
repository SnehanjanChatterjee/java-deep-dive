import java.util.concurrent.TimeUnit;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/** Checks timeout, interruption and drain-on-close contracts. @author Snehanjan Chatterjee */
public final class ConditionMailboxTest {
    @Test public void testTimeoutDeliveryAndClosure() throws InterruptedException {
        final ConditionMailbox<String> mailbox = new ConditionMailbox<>();
        assertNull(mailbox.poll(0, TimeUnit.NANOSECONDS)); mailbox.put("value"); mailbox.close();
        assertEquals("value", mailbox.poll(0, TimeUnit.NANOSECONDS)); assertNull(mailbox.poll(1, TimeUnit.SECONDS));
        assertThrows(IllegalStateException.class, () -> mailbox.put("late"));
    }
    @Test public void testRejectedArgumentsAndInterruptionPreserveItem() throws InterruptedException {
        final ConditionMailbox<String> mailbox = new ConditionMailbox<>();
        assertThrows(NullPointerException.class, () -> mailbox.put(null));
        assertThrows(NullPointerException.class, () -> mailbox.poll(1, null));
        assertThrows(IllegalArgumentException.class, () -> mailbox.poll(-1, TimeUnit.SECONDS));
        mailbox.put("retained");
        Thread.currentThread().interrupt();
        try { assertThrows(InterruptedException.class, () -> mailbox.poll(1, TimeUnit.SECONDS)); }
        finally { Thread.interrupted(); }
        assertEquals("retained", mailbox.poll(0, TimeUnit.NANOSECONDS));
    }
}
