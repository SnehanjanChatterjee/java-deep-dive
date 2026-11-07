import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

/** Verifies cleanup continuation and preservation of the primary failure. @author Snehanjan Chatterjee */
public final class FilePathLabTest {
    @Test public void testAttemptsLaterPathAndPreservesBodyFailure() throws IOException {
        final Path root = Files.createTempDirectory("path-test-");
        final Path blocked = Files.createDirectory(root.resolve("blocked"));
        final Path child = Files.createFile(blocked.resolve("child"));
        final Path removable = Files.createFile(root.resolve("removable"));
        final IOException body = new IOException("body failure");
        try {
            try (final FilePathLab.OwnedPaths cleanup = new FilePathLab.OwnedPaths(blocked, removable)) { throw body; }
            catch (final IOException observed) {
                assertSame(body, observed);
                assertEquals(1, observed.getSuppressed().length);
                assertFalse(Files.exists(removable));
            }
        } finally {
            Files.deleteIfExists(child); Files.deleteIfExists(blocked); Files.deleteIfExists(removable); Files.deleteIfExists(root);
        }
    }
    @Test public void testCollectsCleanupFailuresAndValidatesPaths() throws IOException {
        final Path root = Files.createTempDirectory("path-test-");
        final Path one = Files.createDirectory(root.resolve("one")), two = Files.createDirectory(root.resolve("two"));
        final Path first = Files.createFile(one.resolve("child")), second = Files.createFile(two.resolve("child"));
        try {
            final IOException failure = assertThrows(IOException.class, () -> new FilePathLab.OwnedPaths(one, two).close());
            assertEquals(1, failure.getSuppressed().length);
            assertThrows(NullPointerException.class, () -> new FilePathLab.OwnedPaths((Path[]) null));
            assertThrows(NullPointerException.class, () -> new FilePathLab.OwnedPaths((Path) null));
        } finally {
            Files.deleteIfExists(first); Files.deleteIfExists(second); Files.deleteIfExists(one); Files.deleteIfExists(two); Files.deleteIfExists(root);
        }
    }
}
