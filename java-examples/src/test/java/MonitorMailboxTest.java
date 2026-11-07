import java.util.concurrent.atomic.AtomicReference;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/** Checks guarded communication, closure and invalid input. @author Snehanjan Chatterjee */
public final class MonitorMailboxTest {
    @Test public void testDrainOnCloseAndRejectLateWrites() throws InterruptedException {
        final MonitorMailbox<String> mailbox = new MonitorMailbox<>();
        assertThrows(NullPointerException.class, () -> mailbox.put(null));
        mailbox.put("value"); mailbox.close(); mailbox.close();
        assertEquals("value", mailbox.take()); assertNull(mailbox.take());
        assertThrows(IllegalStateException.class, () -> mailbox.put("late"));
    }
    @Test public void testConsumerReceivesPublishedValue() throws InterruptedException {
        final MonitorMailbox<String> mailbox = new MonitorMailbox<>(); final AtomicReference<String> result = new AtomicReference<>();
        final Thread consumer = new Thread(() -> { try { result.set(mailbox.take()); } catch (final InterruptedException interrupted) { Thread.currentThread().interrupt(); } });
        consumer.setDaemon(true); consumer.start();
        try { mailbox.put("received"); consumer.join(5000); assertFalse(consumer.isAlive()); assertEquals("received", result.get()); }
        finally { mailbox.close(); consumer.interrupt(); consumer.join(5000); }
    }
}
