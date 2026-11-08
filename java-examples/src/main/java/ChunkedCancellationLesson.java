/** Checks cancellation between complete CPU chunks without an interruptible wait. @author Snehanjan Chatterjee */
public final class ChunkedCancellationLesson {
    public static final class Worker implements Runnable {
        private final int requestedChunks;
        private final int cancelAfter;
        private int completed;
        private long checksum;
        private boolean cleaned;

        /** Uses -1 for no controlled request, or requests interruption after the selected count. */
        public Worker(final int requestedChunks, final int cancelAfter) {
            if (requestedChunks < 0 || cancelAfter < -1) {
                throw new IllegalArgumentException("invalid chunk or cancellation count");
            }
            this.requestedChunks = requestedChunks;
            this.cancelAfter = cancelAfter;
        }

        /** Commits only complete chunks and checks status before starting another. */
        @Override public void run() {
            if (cancelAfter == 0) {
                Thread.currentThread().interrupt();
            }
            try {
                while (completed < requestedChunks && !Thread.currentThread().isInterrupted()) {
                    long chunk = 0;
                    for (int value = 1; value <= 100; value++) {
                        chunk += value;
                    }
                    checksum += chunk;
                    completed++;
                    if (completed == cancelAfter) {
                        Thread.currentThread().interrupt();
                    }
                }
            } finally {
                cleaned = true;
            }
        }

        /** Requires the caller to have joined this worker's Thread. */
        public int completed() {
            return completed;
        }

        /** Requires the caller to have joined this worker's Thread. */
        public long checksum() {
            return checksum;
        }

        /** Requires the caller to have joined this worker's Thread. */
        public boolean cleaned() {
            return cleaned;
        }
    }

    /** A controlled request after the third commit makes the chosen checkpoint reproducible. */
    public static void main(final String[] args) throws InterruptedException {
        final Worker task = new Worker(5, 3);
        final Thread worker = new Thread(task, "chunk-worker");
        worker.start();
        worker.join();
        System.out.println("completed=" + task.completed());
        System.out.println("checksum=" + task.checksum());
        System.out.println("cleanup=" + task.cleaned());
    }
}
