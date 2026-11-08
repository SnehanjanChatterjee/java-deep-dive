/** Compares four ways to supply the same kind of Runnable behavior. @author Snehanjan Chatterjee */
public final class ThreadConstructionLesson {
    private static final class NamedTask implements Runnable {
        private final String[] answers;

        private NamedTask(final String[] answers) {
            this.answers = answers;
        }

        @Override public void run() {
            answers[1] = "named Runnable";
        }
    }

    private static final class NamedWorker extends Thread {
        private final String[] answers;

        private NamedWorker(final String[] answers) {
            super("subclass-worker");
            this.answers = answers;
        }

        @Override public void run() {
            answers[0] = "Thread subclass";
        }
    }

    /** Starts each finite task and joins all workers before returning their answers. */
    public static String[] runForms() throws InterruptedException {
        final String[] answers = new String[4];
        final Thread subclass = new NamedWorker(answers);
        final Thread named = new Thread(new NamedTask(answers), "named-worker");
        final Runnable anonymous = new Runnable() {
            @Override public void run() {
                answers[2] = "anonymous Runnable";
            }
        };
        final Thread inline = new Thread(anonymous, "anonymous-worker");
        final Thread lambda = new Thread(() -> answers[3] = "lambda Runnable", "lambda-worker");
        subclass.start();
        named.start();
        inline.start();
        lambda.start();
        subclass.join();
        named.join();
        inline.join();
        lambda.join();
        return answers;
    }

    /** Prints results in array order, independently of the workers' execution order. */
    public static void main(final String[] args) throws InterruptedException {
        for (final String answer : runForms()) {
            System.out.println(answer);
        }
    }
}
