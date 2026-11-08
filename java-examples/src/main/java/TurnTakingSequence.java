/** Uses one guarded predicate for finite two-worker or three-worker turn-taking. @author Snehanjan Chatterjee */
public final class TurnTakingSequence {
    private final int participants;
    private final int limit;
    private final StringBuilder trace = new StringBuilder();
    private long next = 1;
    private boolean cancelled;

    private TurnTakingSequence(final int participants, final int limit) {
        this.participants = participants;
        this.limit = limit;
    }

    /** Rechecks turn, exhaustion and cancellation under the same monitor after every wakeup. */
    private synchronized void takeTurns(final int role) throws InterruptedException {
        while (!cancelled && next <= limit) {
            while (!cancelled && next <= limit && (next - 1) % participants != role) {
                wait();
            }
            if (cancelled || next > limit) {
                return;
            }
            if (trace.length() > 0) {
                trace.append(' ');
            }
            final String label = participants == 2 ? (role == 0 ? "odd" : "even")
                : String.valueOf((char) ('A' + role));
            trace.append(label).append('=').append(next);
            next++;
            notifyAll();
        }
    }

    /** Makes cancellation observable to all participants, including those waiting for a turn. */
    private synchronized void cancel() {
        cancelled = true;
        notifyAll();
    }

    /** Completes cleanup after an interrupted owner; repeated interrupts are restored afterward. */
    private static void joinForCleanup(final Thread[] workers) {
        boolean interruptedAgain = false;
        for (final Thread worker : workers) {
            while (worker.isAlive()) {
                try {
                    worker.join();
                } catch (final InterruptedException repeated) {
                    interruptedAgain = true;
                }
            }
        }
        if (interruptedAgain) {
            Thread.currentThread().interrupt();
        }
    }

    /** Returns a complete ordered trace; owner interruption cancels and joins owned workers. */
    public static String run(final int participants, final int limit) throws InterruptedException {
        if (participants != 2 && participants != 3) {
            throw new IllegalArgumentException("participants must be two or three");
        }
        if (limit < 0) {
            throw new IllegalArgumentException("limit must be nonnegative");
        }
        if (Thread.interrupted()) {
            throw new InterruptedException("owner interrupted before starting workers");
        }
        final TurnTakingSequence sequence = new TurnTakingSequence(participants, limit);
        final Thread[] workers = new Thread[participants];
        for (int index = 0; index < participants; index++) {
            final int role = index;
            workers[index] = new Thread(() -> {
                try {
                    sequence.takeTurns(role);
                } catch (final InterruptedException interrupted) {
                    sequence.cancel();
                    Thread.currentThread().interrupt();
                }
            }, "sequence-worker-" + role);
        }
        boolean completed = false;
        try {
            for (final Thread worker : workers) {
                worker.start();
            }
            for (final Thread worker : workers) {
                worker.join();
            }
            if (sequence.cancelled) {
                throw new InterruptedException("a sequence worker was interrupted");
            }
            completed = true;
            return sequence.trace.toString();
        } finally {
            if (!completed) {
                sequence.cancel();
                for (final Thread worker : workers) {
                    worker.interrupt();
                }
                joinForCleanup(workers);
            }
        }
    }

    /** Runs odd/even roles and then three fixed roles without sleeps or thread-name decisions. */
    public static void main(final String[] args) throws InterruptedException {
        System.out.println(run(2, 7));
        System.out.println(run(3, 8));
    }
}
