import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;

/** Performs ordinary file operations with explicit ownership and text encoding. @author Snehanjan Chatterjee */
public final class FilePathLab {
    /** Creates, writes, reads, copies and moves only temporary local files. */
    public static void main(final String[] args) throws IOException {
        final Path directory = Files.createTempDirectory("java-path-lesson-");
        final Path original = directory.resolve("names.txt");
        final Path copy = directory.resolve("copy.txt");
        final Path archived = directory.resolve("archived.txt");
        try (final OwnedPaths cleanup = new OwnedPaths(original, copy, archived, directory)) {
            try (final BufferedWriter writer = Files.newBufferedWriter(original, StandardCharsets.UTF_8)) {
                writer.write("Mira"); writer.newLine(); writer.write("Noor");
            }
            try (final BufferedReader reader = Files.newBufferedReader(original, StandardCharsets.UTF_8)) {
                String line;
                while ((line = reader.readLine()) != null) { System.out.println(line); }
            }
            Files.copy(original, copy);
            Files.move(original, archived);
            try (final Stream<Path> entries = Files.list(directory)) { System.out.println(entries.count()); }
        }
    }

    static final class OwnedPaths implements AutoCloseable {
        private final Path[] paths;
        /** Records non-null owned paths in their required deletion order. */
        OwnedPaths(final Path... paths) {
            this.paths = Objects.requireNonNull(paths, "paths").clone();
            for (final Path path : this.paths) { Objects.requireNonNull(path, "path"); }
        }
        /** Attempts every path and retains later cleanup failures as suppressed exceptions. */
        @Override public void close() throws IOException {
            IOException failure = null;
            for (final Path path : paths) {
                try { Files.deleteIfExists(path); }
                catch (final IOException problem) {
                    if (failure == null) { failure = problem; }
                    else if (failure != problem) { failure.addSuppressed(problem); }
                }
            }
            if (failure != null) { throw failure; }
        }
    }
}
