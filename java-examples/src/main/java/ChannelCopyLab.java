import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/** Demonstrates bounded buffering and partial-write handling on blocking channels. @author Snehanjan Chatterjee */
public final class ChannelCopyLab {
    /** Copies between caller-owned blocking channels without closing them. */
    static long copy(final ReadableByteChannel source, final WritableByteChannel destination) throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocate(8);
        long count = 0;
        while (source.read(buffer) != -1) {
            buffer.flip();
            while (buffer.hasRemaining()) { count += destination.write(buffer); }
            buffer.clear();
        }
        return count;
    }

    /** Creates and cleans up a pair of temporary demonstration files. */
    public static void main(final String[] args) throws IOException {
        final Path source = Files.createTempFile("copy-source-", ".txt");
        try {
            final Path destination = Files.createTempFile("copy-destination-", ".txt");
            try {
                Files.write(source, "hello".getBytes(StandardCharsets.UTF_8));
                try (final FileChannel input = FileChannel.open(source, StandardOpenOption.READ);
                     final FileChannel output = FileChannel.open(destination, StandardOpenOption.WRITE,
                             StandardOpenOption.TRUNCATE_EXISTING)) {
                    System.out.println("copied " + copy(input, output) + " bytes");
                }
                System.out.println(new String(Files.readAllBytes(destination), StandardCharsets.UTF_8));
            } finally { Files.deleteIfExists(destination); }
        } finally { Files.deleteIfExists(source); }
    }
}
