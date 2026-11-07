/** Creates bounded live retention while allocating enough short-lived arrays to inspect GC. @author Snehanjan Chatterjee */
public final class GarbageCollectionWorkload {
    private static final byte[][] RETAINED = new byte[16][];

    /** Replaces a bounded window of buffers; older arrays lose this owner's reference. */
    public static void main(final String[] args) {
        for (int i = 0; i < 2000; i++) {
            final byte[] buffer = new byte[64 * 1024];
            buffer[0] = 1;
            RETAINED[i % RETAINED.length] = buffer;
        }
        long bytes = 0;
        for (final byte[] buffer : RETAINED) { bytes += buffer.length; }
        System.out.println("retained buffers=" + RETAINED.length);
        System.out.println("retained bytes=" + bytes);
    }
}
