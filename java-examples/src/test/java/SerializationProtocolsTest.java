import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.NotSerializableException;
import java.lang.reflect.Field;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public final class SerializationProtocolsTest {
    @Test public void testRepeatedRootUsesHandleUntilReset() throws Exception {
        final SerializationProtocols.MutableValue[] roots = SerializationProtocols.repeatedRoots();
        assertSame(roots[0], roots[1]);
        assertEquals(1, roots[1].value);
        assertNotSame(roots[0], roots[2]);
        assertEquals(2, roots[2].value);
    }
    @Test public void testExternalScoreBoundariesRoundTrip() throws Exception {
        for (final int value : new int[] {0, 7, 100}) {
            final SerializationProtocols.ExternalScore original = new SerializationProtocols.ExternalScore(value);
            final SerializationProtocols.ExternalScore restored = (SerializationProtocols.ExternalScore) SerializationProtocols.copy(original);
            assertNotSame(original, restored);
            assertEquals(value, restored.value());
        }
    }
    @Test public void testConstructorAssignedTransientFinalFieldIsOmitted() throws Exception {
        final SerializationProtocols.TransientFinalValue restored = (SerializationProtocols.TransientFinalValue) SerializationProtocols.copy(new SerializationProtocols.TransientFinalValue(12));
        assertEquals(0, restored.value());
    }
    @Test public void testTransientDerivedStateIsRebuilt() throws Exception {
        final SerializationProtocols.CustomScore score = (SerializationProtocols.CustomScore) SerializationProtocols.copy(new SerializationProtocols.CustomScore(9));
        assertEquals("score:9", score.label());
    }
    @Test public void testReadResolveSuppliesCanonicalIdentity() throws Exception {
        assertSame(SerializationProtocols.CanonicalToken.INSTANCE, SerializationProtocols.copy(SerializationProtocols.CanonicalToken.INSTANCE));
    }
    @Test public void testNullRootRoundTrip() throws Exception { assertNull(SerializationProtocols.copy(null)); }
    @Test(expected = IllegalArgumentException.class) public void testInvalidSourceScoreRejected() { new SerializationProtocols.ExternalScore(-1); }
    @Test(expected = IllegalArgumentException.class) public void testExcessiveSourceScoreRejected() { new SerializationProtocols.ExternalScore(101); }
    @Test(expected = IllegalArgumentException.class) public void testInvalidCustomScoreConstructionRejected() { new SerializationProtocols.CustomScore(-1); }
    @Test(expected = NotSerializableException.class) public void testNonSerializableRootRejected() throws Exception { SerializationProtocols.copy(new Object()); }
    @Test(expected = InvalidObjectException.class) public void testCustomReadHookRejectsInvalidRestoredField() throws Exception {
        final SerializationProtocols.CustomScore score = new SerializationProtocols.CustomScore(9);
        final Field value = SerializationProtocols.CustomScore.class.getDeclaredField("value");
        value.setAccessible(true);
        value.setInt(score, -1);
        SerializationProtocols.copy(score);
    }
    @Test(expected = InvalidObjectException.class) public void testUnknownExternalFormatRejected() throws Exception { readExternal(2, 7); }
    @Test(expected = InvalidObjectException.class) public void testInvalidRestoredScoreRejected() throws Exception { readExternal(1, -1); }
    @Test(expected = InvalidObjectException.class) public void testExcessiveRestoredScoreRejected() throws Exception { readExternal(1, 101); }

    private static void readExternal(final int version, final int value) throws Exception {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream output = new ObjectOutputStream(bytes)) { output.writeInt(version); output.writeInt(value); }
        try (ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            new SerializationProtocols.ExternalScore().readExternal(input);
        }
    }
}
