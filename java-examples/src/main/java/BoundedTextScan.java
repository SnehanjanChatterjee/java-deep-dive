import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Objects;

/** Counts nonblank lines without retaining complete lines or closing a borrowed reader. @author Snehanjan Chatterjee */
public final class BoundedTextScan {
    /** Counts CR/LF-delimited nonblank lines under a maximum UTF-16-unit input budget. */
    public static long countNonblank(final Reader reader, final int maximumChars) throws IOException {
        Objects.requireNonNull(reader, "reader");
        if (maximumChars < 0) { throw new IllegalArgumentException("negative character limit"); }
        final char[] buffer = new char[512];
        long consumed = 0, nonblank = 0;
        boolean currentHasText = false;
        while (true) {
            final int requested = (int) Math.min(buffer.length, (long) maximumChars - consumed + 1);
            final int count = reader.read(buffer, 0, requested);
            if (count == -1) { return nonblank + (currentHasText ? 1 : 0); }
            if (count == 0) { throw new IOException("reader made no progress"); }
            consumed += count;
            if (consumed > maximumChars) { throw new IOException("character limit exceeded"); }
            for (int index = 0; index < count; index++) {
                final char value = buffer[index];
                if (value == '\r' || value == '\n') {
                    if (currentHasText) { nonblank++; }
                    currentHasText = false;
                } else if (value > ' ') { currentHasText = true; }
            }
        }
    }

    /** Shows a final unterminated line and a limit failure using owned in-memory readers. */
    public static void main(final String[] args) throws IOException {
        try (final StringReader reader = new StringReader(" Mira \r\n \nNoor")) {
            System.out.println("nonblank=" + countNonblank(reader, 30));
        }
        try (final StringReader reader = new StringReader("Mira")) {
            try { countNonblank(reader, 3); }
            catch (final IOException expected) { System.out.println("character limit rejected"); }
        }
    }
}
