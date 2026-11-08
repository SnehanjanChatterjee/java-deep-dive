/** Distinguishes invoking a virtual Thread's run from starting its execution. @author Snehanjan Chatterjee */
public final class VirtualInvocationLesson {
    /** Returns observations before start and after joined virtual execution. */
    public static String[] observations() throws InterruptedException {
        final int[] calls = {0};
        final Runnable task = () -> calls[0]++;
        final Thread worker = Thread.ofVirtual().unstarted(task);
        worker.run();
        final String direct = "after virtual run: calls=" + calls[0] + ", state=" + worker.getState();
        worker.start();
        worker.join();
        return new String[] {direct, "after start/join: calls=" + calls[0] + ", virtual=" + worker.isVirtual()};
    }

    /** Requires stable Java 21 APIs, with no preview flags. */
    public static void main(final String[] args) throws InterruptedException {
        for (final String observation : observations()) {
            System.out.println(observation);
        }
    }
}
