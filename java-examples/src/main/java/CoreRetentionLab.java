import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/** Makes a concrete strong retaining path inspectable without asserting GC timing. @author Snehanjan Chatterjee */
public final class CoreRetentionLab {
    private static final Map<Integer, byte[]> RETAINED = new HashMap<>();
    /** Adds/replaces buffers within a declared key interval. */
    private static void load(final int first, final int limit) {
        for (int key = first; key < limit; key++) { RETAINED.put(key, new byte[64 * 1024]); }
    }
    /** Shows stable-key replacement versus growing key retention; optional hold supports local inspection. */
    public static void main(final String[] args) throws IOException {
        load(0, 16); System.out.println(RETAINED.size());
        load(0, 16); System.out.println(RETAINED.size());
        load(16, 32); System.out.println(RETAINED.size());
        if (args.length > 0 && "hold".equals(args[0])) {
            System.out.println("Inspect CoreRetentionLab using jcmd; press Enter to clear.");
            System.in.read();
        }
        RETAINED.clear();
        System.out.println(RETAINED.size());
    }
}
