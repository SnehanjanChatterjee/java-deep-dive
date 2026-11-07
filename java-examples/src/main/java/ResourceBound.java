import java.util.concurrent.Semaphore;

/** Separate lightweight task execution from limits on external resources. @author Snehanjan Chatterjee */
public final class ResourceBound {

    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        final Semaphore permits = new Semaphore(2);
        permits.acquire();
        try { System.out.println(permits.availablePermits()); } finally { permits.release(); }
        System.out.println(permits.availablePermits());
    }
}
