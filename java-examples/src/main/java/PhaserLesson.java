import java.util.concurrent.Phaser;

/** Registers child participation before starting dynamically owned work. @author Snehanjan Chatterjee */
public final class PhaserLesson {
    /** Keeps the parent registered until it has observed child phase completion. */
    public static void main(final String[] args) throws InterruptedException {
        final Phaser phaser = new Phaser(1);
        final int[] results = new int[2];
        final Thread[] children = new Thread[2];
        for (int index = 0; index < children.length; index++) {
            final int slot = index;
            phaser.register();
            children[index] = new Thread(() -> {
                try { results[slot] = 20 + 2 * slot; }
                finally { phaser.arriveAndDeregister(); }
            });
        }
        System.out.println("registered before=" + phaser.getRegisteredParties());
        for (final Thread child : children) { child.start(); }
        phaser.arriveAndAwaitAdvance();
        for (final Thread child : children) { child.join(); }
        System.out.println("total=" + (results[0] + results[1]));
        System.out.println("registered after=" + phaser.getRegisteredParties());
        phaser.arriveAndDeregister();
        System.out.println("terminated=" + phaser.isTerminated());
    }
}
