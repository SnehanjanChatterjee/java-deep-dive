/** Shows that joining an unstarted thread does not arrange its future start. @author Snehanjan Chatterjee */
public final class JoinSchedulingLesson {
    /** Returns stable observations on each side of the worker's start and termination. */
    public static String[] observations() throws InterruptedException {
        final int[] answer = new int[1];
        final Thread worker = new Thread(() -> answer[0] = 42, "joined-worker");
        worker.join();
        final String beforeStart = "after early join=" + worker.getState() + ", answer=" + answer[0];
        worker.start();
        worker.join();
        return new String[] {beforeStart,
            "after started join=" + worker.getState() + ", answer=" + answer[0]};
    }

    /** Prints the completed observations instead of relying on a sleep duration. */
    public static void main(final String[] args) throws InterruptedException {
        for (final String observation : observations()) {
            System.out.println(observation);
        }
    }
}
