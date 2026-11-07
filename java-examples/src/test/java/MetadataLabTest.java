import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/** Protects proxy identity and target-failure contracts. @author Snehanjan Chatterjee */
public final class MetadataLabTest {
    private static final class FailingGreeter implements MetadataLab.Greeter {
        private final IllegalArgumentException failure = new IllegalArgumentException("greeting failed");
        /** Throws the exact failure used to inspect invocation propagation. */
        @Override public String greet(final String name) { throw failure; }
    }
    private static final class ExplosiveStringGreeter implements MetadataLab.Greeter {
        /** Supplies the ordinary service capability. */
        @Override public String greet(final String name) { return "Hello " + name; }
        /** Fails if the proxy incorrectly delegates its diagnostic representation. */
        @Override public String toString() { throw new IllegalStateException("target toString must not run"); }
    }

    @Test public void testIdentityEqualityAndHashCode() {
        final MetadataLab.PlainGreeter target = new MetadataLab.PlainGreeter();
        final MetadataLab.Greeter first = MetadataLab.createProxy(target);
        final MetadataLab.Greeter second = MetadataLab.createProxy(target);
        assertTrue(first.equals(first));
        assertFalse(first.equals(second)); assertFalse(second.equals(first));
        assertFalse(first.equals(target)); assertFalse(target.equals(first)); assertFalse(first.equals(null));
        assertEquals(System.identityHashCode(first), first.hashCode());
        assertEquals(first.hashCode(), first.hashCode());
    }

    @Test public void testToStringAndServiceDelegation() {
        final MetadataLab.Greeter proxy = MetadataLab.createProxy(new ExplosiveStringGreeter());
        assertTrue(proxy.toString().startsWith("GreeterProxy("));
        assertTrue(proxy.toString().contains("ExplosiveStringGreeter"));
        assertEquals("Hello Mira", proxy.greet("Mira"));
    }

    @Test public void testOriginalServiceFailureAndNullTarget() {
        final FailingGreeter target = new FailingGreeter();
        final MetadataLab.Greeter proxy = MetadataLab.createProxy(target);
        assertSame(target.failure, assertThrows(IllegalArgumentException.class, () -> proxy.greet("Mira")));
        assertThrows(NullPointerException.class, () -> MetadataLab.createProxy(null));
    }
}
