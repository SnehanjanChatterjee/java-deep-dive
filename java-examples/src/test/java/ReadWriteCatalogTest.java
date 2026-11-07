import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/** Verifies snapshot ownership and concurrent writer exclusion. @author Snehanjan Chatterjee */
public final class ReadWriteCatalogTest {
    @Test public void testSnapshotSurvivesLaterWrites() {
        final ReadWriteCatalog catalog = new ReadWriteCatalog();
        assertTrue(catalog.snapshot().isEmpty());
        catalog.put("java", "Language"); final Map<String, String> earlier = catalog.snapshot();
        catalog.put("java", "Updated"); catalog.put("jvm", "Runtime");
        assertEquals("Language", earlier.get("java")); assertEquals(1, earlier.size());
        assertEquals("Updated", catalog.snapshot().get("java"));
        assertThrows(UnsupportedOperationException.class, () -> earlier.put("late", "value"));
    }
    @Test public void testRejectsNullBeforeMutation() {
        final ReadWriteCatalog catalog = new ReadWriteCatalog();
        assertThrows(NullPointerException.class, () -> catalog.put(null, "value"));
        assertThrows(NullPointerException.class, () -> catalog.put("key", null));
        assertTrue(catalog.snapshot().isEmpty());
    }
    @Test public void testConcurrentWritersPreserveAllIndependentKeys() throws InterruptedException {
        final ReadWriteCatalog catalog = new ReadWriteCatalog();
        final Thread first = new Thread(() -> { for (int index = 0; index < 100; index++) { catalog.put("A" + index, "left"); } });
        final Thread second = new Thread(() -> { for (int index = 0; index < 100; index++) { catalog.put("B" + index, "right"); } });
        first.start(); second.start(); first.join(); second.join();
        assertEquals(200, catalog.snapshot().size());
    }
}
