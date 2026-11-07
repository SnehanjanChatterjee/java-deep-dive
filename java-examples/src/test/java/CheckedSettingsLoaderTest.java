import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

/** Exercises opening, parsing, body/close failure and ownership. @author Snehanjan Chatterjee */
public final class CheckedSettingsLoaderTest {
    private static final class TrackingReader extends Reader {
        private final StringReader input;
        private final IOException readFailure;
        private final IOException closeFailure;
        private boolean closed;
        private TrackingReader(final String text, final IOException readFailure, final IOException closeFailure) {
            input = new StringReader(text); this.readFailure = readFailure; this.closeFailure = closeFailure;
        }
        @Override public int read(final char[] buffer, final int offset, final int length) throws IOException {
            if (readFailure != null) { throw readFailure; }
            return input.read(buffer, offset, length);
        }
        @Override public void close() throws IOException {
            closed = true; input.close();
            if (closeFailure != null) { throw closeFailure; }
        }
    }
    private static CheckedSettingsLoader.ReaderFactory factory(final Reader reader) {
        return new CheckedSettingsLoader.ReaderFactory() {
            @Override public Reader open() { return reader; }
        };
    }
    @Test public void testSuccessClosesOwnedReader() throws CheckedSettingsLoader.SettingsLoadException {
        final TrackingReader reader = new TrackingReader(" 8080\n", null, null);
        assertEquals(8080, CheckedSettingsLoader.loadPort(factory(reader)));
        assertTrue(reader.closed);
        assertEquals(1, CheckedSettingsLoader.loadPort(factory(new StringReader("1"))));
        assertEquals(65_535, CheckedSettingsLoader.loadPort(factory(new StringReader("65535"))));
    }
    @Test public void testOpeningAndReadingFailurePreserveIdentity() {
        final IOException opening = new IOException("open");
        final CheckedSettingsLoader.SettingsLoadException openFailure = assertThrows(
                CheckedSettingsLoader.SettingsLoadException.class, () -> CheckedSettingsLoader.loadPort(
                        new CheckedSettingsLoader.ReaderFactory() {
                            @Override public Reader open() throws IOException { throw opening; }
                        }));
        assertSame(opening, openFailure.getCause());
        final IOException reading = new IOException("read");
        final TrackingReader reader = new TrackingReader("", reading, null);
        final CheckedSettingsLoader.SettingsLoadException readFailure = assertThrows(
                CheckedSettingsLoader.SettingsLoadException.class, () -> CheckedSettingsLoader.loadPort(factory(reader)));
        assertSame(reading, readFailure.getCause()); assertTrue(reader.closed);
    }
    @Test public void testCloseOnlyAndSimultaneousFailures() {
        final IOException closing = new IOException("close");
        final CheckedSettingsLoader.SettingsLoadException closeOnly = assertThrows(
                CheckedSettingsLoader.SettingsLoadException.class, () -> CheckedSettingsLoader.loadPort(
                        factory(new TrackingReader("8080", null, closing))));
        assertSame(closing, closeOnly.getCause());
        final IOException reading = new IOException("read");
        final CheckedSettingsLoader.SettingsLoadException both = assertThrows(
                CheckedSettingsLoader.SettingsLoadException.class, () -> CheckedSettingsLoader.loadPort(
                        factory(new TrackingReader("", reading, closing))));
        assertSame(reading, both.getCause());
        assertSame(closing, both.getCause().getSuppressed()[0]);
    }
    @Test public void testParsingRangeMissingAndNullContracts() {
        for (final String text : new String[] {"", "bad", "0", "65536"}) {
            assertThrows(CheckedSettingsLoader.SettingsLoadException.class,
                    () -> CheckedSettingsLoader.loadPort(factory(new StringReader(text))));
        }
        assertThrows(NullPointerException.class, () -> CheckedSettingsLoader.loadPort(null));
        assertThrows(NullPointerException.class, () -> CheckedSettingsLoader.loadPort(factory(null)));
    }
}
