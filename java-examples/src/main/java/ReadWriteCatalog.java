import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/** Protects a mutable catalog while returning independently owned snapshots. @author Snehanjan Chatterjee */
public final class ReadWriteCatalog {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<String, String> entries = new TreeMap<>();
    /** Installs one mapping under exclusive write ownership. */
    public void put(final String key, final String value) {
        Objects.requireNonNull(key, "key"); Objects.requireNonNull(value, "value");
        lock.writeLock().lock();
        try { entries.put(key, value); }
        finally { lock.writeLock().unlock(); }
    }
    /** Copies all mappings while writers are excluded. */
    public Map<String, String> snapshot() {
        lock.readLock().lock();
        try { return Collections.unmodifiableMap(new TreeMap<>(entries)); }
        finally { lock.readLock().unlock(); }
    }
    /** Shows that an earlier snapshot does not change after a catalog update. */
    public static void main(final String[] args) {
        final ReadWriteCatalog catalog = new ReadWriteCatalog();
        catalog.put("java", "Language");
        final Map<String, String> earlier = catalog.snapshot();
        catalog.put("jvm", "Runtime");
        System.out.println(earlier);
        System.out.println(catalog.snapshot());
    }
}
