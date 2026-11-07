import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/** Exercises the proxy boundary rather than reflection implementation details. @author Snehanjan Chatterjee */
public final class ProxyLessonTest {
    @Test public void testDelegationAndIdentity() throws IOException {
        final ProxyLesson.Greeting target = name -> "Hello " + name;
        final ProxyLesson.Greeting proxy = ProxyLesson.wrap(target);
        assertEquals("Hello Mira", proxy.greet("Mira"));
        assertTrue(proxy.equals(proxy)); assertFalse(proxy.equals(target)); assertFalse(proxy.equals(null));
        assertEquals(System.identityHashCode(proxy), proxy.hashCode());
        assertEquals("Greeting proxy", proxy.toString());
    }
    @Test public void testOriginalCheckedFailureAndNullTarget() {
        final IOException cause = new IOException("target failed");
        final ProxyLesson.Greeting proxy = ProxyLesson.wrap(name -> { throw cause; });
        assertSame(cause, assertThrows(IOException.class, () -> proxy.greet("Mira")));
        assertThrows(NullPointerException.class, () -> ProxyLesson.wrap(null));
    }
}
