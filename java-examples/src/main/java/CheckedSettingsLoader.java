import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Objects;

/** Owns a reader and preserves checked loading failures and their causes. @author Snehanjan Chatterjee */
public final class CheckedSettingsLoader {
    /** Supplies a fresh reader whose ownership transfers to the loader. */
    public interface ReaderFactory {
        /** Opens the input or reports a checked opening failure. */
        Reader open() throws IOException;
    }
    /** Reports opening, reading, parsing or closing failure with the original cause. */
    public static final class SettingsLoadException extends Exception {
        private static final long serialVersionUID = 1L;
        /** Preserves the original failure as a cause. */
        SettingsLoadException(final String message, final Throwable cause) { super(message, cause); }
    }

    /** Reads one port in 1..65535 and closes the opened reader on every exit path. */
    public static int loadPort(final ReaderFactory factory) throws SettingsLoadException {
        Objects.requireNonNull(factory, "factory");
        try (final BufferedReader reader = new BufferedReader(Objects.requireNonNull(factory.open(), "reader"))) {
            final String text = reader.readLine();
            if (text == null) {
                throw new IllegalArgumentException("missing port");
            }
            final int port = Integer.parseInt(text.trim());
            if (port < 1 || port > 65_535) {
                throw new IllegalArgumentException("port out of range");
            }
            return port;
        } catch (final IOException | IllegalArgumentException cause) {
            throw new SettingsLoadException("Cannot load port", cause);
        }
    }

    /** Demonstrates a named factory and checked failure handling without external files. */
    public static void main(final String[] args) throws SettingsLoadException {
        final ReaderFactory factory = new ReaderFactory() {
            @Override public Reader open() { return new StringReader("8080\n"); }
        };
        System.out.println(loadPort(factory));
        try {
            loadPort(new ReaderFactory() {
                @Override public Reader open() throws IOException { throw new IOException("not available"); }
            });
        } catch (final SettingsLoadException failure) {
            System.out.println(failure.getMessage());
            System.out.println(failure.getCause().getMessage());
        }
    }
}
