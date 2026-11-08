import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks parsing, EOF, malformed data, I/O propagation and borrowed ownership. */
public final class ConsoleInputLessonTest {
    @Test public void testReadCountLeavesBorrowedReaderUsable() throws IOException {
        try (BufferedReader input = new BufferedReader(new StringReader(" 12 \nnext\n"))) {
            assertEquals(12, ConsoleInputLesson.readCount(input));
            assertEquals("next", input.readLine());
        }
        try (BufferedReader input = new BufferedReader(new StringReader("0\n"))) {
            assertEquals(0, ConsoleInputLesson.readCount(input));
        }
    }
    @Test public void testInvalidAndMissingCounts() throws IOException {
        for (final String text : new String[] {"", "\n", "bad\n", "-1\n", "2147483648\n"}) {
            try (BufferedReader input = new BufferedReader(new StringReader(text))) {
                assertThrows(IllegalArgumentException.class, () -> ConsoleInputLesson.readCount(input));
            }
        }
        assertThrows(NullPointerException.class, () -> ConsoleInputLesson.readCount(null));
        final BufferedReader closed = new BufferedReader(new StringReader("1"));
        closed.close();
        assertThrows(IOException.class, () -> ConsoleInputLesson.readCount(closed));
    }
    @Test public void testScannerTokenAndLineBoundary() {
        assertEquals("12:remaining=[]:name=Ada", ConsoleInputLesson.tokenThenLine("12\nAda\n"));
        assertEquals("12:remaining=[ extra]:name=Ada", ConsoleInputLesson.tokenThenLine("12 extra\nAda\n"));
        assertThrows(NullPointerException.class, () -> ConsoleInputLesson.tokenThenLine(null));
    }
}
