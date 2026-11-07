import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/** Removes circular lock ordering from a two-account transfer. @author Snehanjan Chatterjee */
public final class OrderedTransferLab {
    public static final class Account {
        private static final AtomicLong NEXT_ID = new AtomicLong();
        private final long lockOrder = NEXT_ID.getAndIncrement();
        private int balance;
        /** Creates an account with nonnegative units. */
        public Account(final int balance) {
            if (balance < 0) { throw new IllegalArgumentException("negative balance"); }
            this.balance = balance;
        }
        /** Reads the account under the same monitor used for writes. */
        public synchronized int balance() { return balance; }
    }
    /** Acquires both accounts in one immutable global order before validating and mutating. */
    public static boolean transfer(final Account from, final Account to, final int amount) {
        Objects.requireNonNull(from, "from"); Objects.requireNonNull(to, "to");
        if (amount <= 0) { throw new IllegalArgumentException("positive amount required"); }
        if (from == to) { return true; }
        final Account first = from.lockOrder < to.lockOrder ? from : to;
        final Account second = first == from ? to : from;
        synchronized (first) {
            synchronized (second) {
                if (from.balance < amount) { return false; }
                final int credited = Math.addExact(to.balance, amount);
                from.balance -= amount; to.balance = credited; return true;
            }
        }
    }
    /** Runs opposing transfers without opposite lock acquisition order. */
    public static void main(final String[] args) throws InterruptedException {
        final Account left = new Account(100); final Account right = new Account(100);
        final Thread a = new Thread(() -> { for (int i = 0; i < 1000; i++) { transfer(left, right, 1); } });
        final Thread b = new Thread(() -> { for (int i = 0; i < 1000; i++) { transfer(right, left, 1); } });
        a.start(); b.start(); a.join(); b.join();
        System.out.println(left.balance() + right.balance());
    }
}
