
/** Measure a defined workload without confusing elapsed time with algorithmic proof. @author Snehanjan Chatterjee */
public final class ObservableWork {

    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        long checksum = 0;
        for (int i = 0; i < 1000; i++) { checksum += i; }
        System.out.println(checksum);
    }
}
