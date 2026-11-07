import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** Manage bytes, text, buffering and network boundaries explicitly. @author Snehanjan Chatterjee */
public final class FilesAndBuffers {

    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        final Path file = Files.createTempFile("java-learning-", ".txt");
        try {
            Files.write(file, "Mira".getBytes(StandardCharsets.UTF_8));
            System.out.println(new String(Files.readAllBytes(file), StandardCharsets.UTF_8));
        } finally { Files.deleteIfExists(file); }
        final ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(42); buffer.flip();
        System.out.println(buffer.getInt());
    }
}
