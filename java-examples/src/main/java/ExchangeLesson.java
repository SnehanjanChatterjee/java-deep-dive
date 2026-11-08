import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/** Exchanges two values at one paired rendezvous. @author Snehanjan Chatterjee */
public final class ExchangeLesson {
    /** Returns each participant's received value after the finite partner has terminated. */
    public static String[] exchangePair() throws InterruptedException, TimeoutException {
        final Exchanger<String> exchanger = new Exchanger<>();
        final String[] received = new String[2];
        final Throwable[] partnerFailure = new Throwable[1];
        final Thread partner = new Thread(() -> {
            try {
                received[1] = exchanger.exchange("worker-buffer", 5, TimeUnit.SECONDS);
            } catch (final InterruptedException interrupted) {
                partnerFailure[0] = interrupted;
                Thread.currentThread().interrupt();
            } catch (final TimeoutException timeout) {
                partnerFailure[0] = timeout;
            }
        }, "exchange-partner");
        partner.start();
        try {
            received[0] = exchanger.exchange("owner-buffer", 5, TimeUnit.SECONDS);
            partner.join();
            if (partnerFailure[0] != null) {
                throw new IllegalStateException("exchange partner failed", partnerFailure[0]);
            }
            return received;
        } finally {
            partner.interrupt();
            partner.join();
        }
    }

    /** Prints the peer value delivered to the owner, then to its partner. */
    public static void main(final String[] args) throws InterruptedException, TimeoutException {
        final String[] received = exchangePair();
        System.out.println("owner received=" + received[0]);
        System.out.println("worker received=" + received[1]);
    }
}
