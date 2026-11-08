import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public final class BoundedTextScanTest {
    @Test public void testNonblankLinesAcrossAllLineEndings() throws IOException {
        assertEquals(3, BoundedTextScan.countNonblank(new StringReader("A\r\n \nB\rC"), 10));
    }
    @Test public void testEmptyAndAllBlankInput() throws IOException {
        assertEquals(0, BoundedTextScan.countNonblank(new StringReader(""), 0));
        assertEquals(0, BoundedTextScan.countNonblank(new StringReader(" \t\r\n"), 4));
    }
    @Test public void testExactLimitAndFinalUnterminatedLine() throws IOException {
        assertEquals(1, BoundedTextScan.countNonblank(new StringReader("ABC"), 3));
    }
    @Test public void testLineStateSurvivesChunkBoundary() throws IOException {
        final StringBuilder input = new StringBuilder();
        for (int index = 0; index < 512; index++) { input.append(' '); }
        input.append('X').append('\n');
        assertEquals(1, BoundedTextScan.countNonblank(new StringReader(input.toString()), 514));
    }
    @Test public void testBorrowedReaderIsNotClosed() throws IOException {
        final boolean[] closed = {false};
        final Reader reader = new StringReader("A") {
            @Override public void close() { closed[0] = true; super.close(); }
        };
        assertEquals(1, BoundedTextScan.countNonblank(reader, 1));
        assertFalse(closed[0]);
        reader.close();
    }
    @Test(expected = IOException.class) public void testOversizedInputRejected() throws IOException { BoundedTextScan.countNonblank(new StringReader("ABCD"), 3); }
    @Test(expected = IOException.class) public void testZeroBudgetRejectsOneCharacter() throws IOException { BoundedTextScan.countNonblank(new StringReader("A"), 0); }
    @Test(expected = IllegalArgumentException.class) public void testNegativeBudgetRejected() throws IOException { BoundedTextScan.countNonblank(new StringReader(""), -1); }
    @Test(expected = NullPointerException.class) public void testNullReaderRejected() throws IOException { BoundedTextScan.countNonblank(null, 1); }
    @Test(expected = IOException.class) public void testReaderFailurePropagates() throws IOException {
        BoundedTextScan.countNonblank(new Reader() {
            @Override public int read(final char[] data, final int offset, final int length) throws IOException { throw new IOException("input failed"); }
            @Override public void close() { }
        }, 10);
    }
    @Test(expected = IOException.class) public void testZeroProgressRejected() throws IOException {
        BoundedTextScan.countNonblank(new Reader() {
            @Override public int read(final char[] data, final int offset, final int length) { return 0; }
            @Override public void close() { }
        }, 10);
    }
}
