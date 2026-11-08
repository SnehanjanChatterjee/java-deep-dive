import java.io.EOFException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public final class SocketLineLessonTest {
    @Test public void testReadOneFrameLeavesNextFrame() throws IOException {
        final StringReader input = new StringReader("hello\nnext\n");
        assertEquals("hello", SocketLineLesson.readFrame(input));
        assertEquals("next", SocketLineLesson.readFrame(input));
    }
    @Test public void testEmptyLineIsAFrame() throws IOException { assertEquals("", SocketLineLesson.readFrame(new StringReader("\n"))); }
    @Test public void testExactMaximumRoundTrip() throws IOException {
        final StringBuilder value = new StringBuilder();
        for (int index = 0; index < 64; index++) { value.append('X'); }
        final StringWriter output = new StringWriter();
        SocketLineLesson.writeFrame(output, value.toString());
        assertEquals(value.toString(), SocketLineLesson.readFrame(new StringReader(output.toString())));
    }
    @Test(expected = EOFException.class) public void testTruncatedLineRejected() throws IOException { SocketLineLesson.readFrame(new StringReader("unfinished")); }
    @Test(expected = IOException.class) public void testOversizedLineRejected() throws IOException {
        final StringBuilder value = new StringBuilder();
        for (int index = 0; index < 65; index++) { value.append('X'); }
        SocketLineLesson.readFrame(new StringReader(value.append('\n').toString()));
    }
    @Test(expected = IllegalArgumentException.class) public void testEmbeddedDelimiterRejected() throws IOException { SocketLineLesson.writeFrame(new StringWriter(), "a\nb"); }
    @Test(expected = NullPointerException.class) public void testNullReaderRejected() throws IOException { SocketLineLesson.readFrame(null); }
    @Test(expected = NullPointerException.class) public void testNullTextRejected() throws IOException { SocketLineLesson.writeFrame(new StringWriter(), null); }
}
