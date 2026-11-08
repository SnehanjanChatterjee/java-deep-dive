import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/** Checks independent type roles and the bounded value's explicit null policy. @author Snehanjan Chatterjee */
public final class GenericRelationshipsTest {
    @Test public void testIndependentComponentsAndBoundedValue() {
        final GenericRelationships.Entry<String, Integer> entry = new GenericRelationships.Entry<>("Ada", 7);
        assertEquals("Ada", entry.key()); assertEquals(Integer.valueOf(7), entry.value());
        final GenericRelationships.Entry<String, Integer> nullable = new GenericRelationships.Entry<>(null, null);
        assertNull(nullable.key()); assertNull(nullable.value());
        final GenericRelationships.NumericValue<Double> value = new GenericRelationships.NumericValue<>(7.5);
        assertEquals(Double.valueOf(7.5), value.value()); assertEquals(7.5, value.asDouble(), 0.0);
    }
    @Test public void testConverterAndRejectedNulls() {
        final GenericRelationships.Converter<String, Integer> converter = new GenericRelationships.Length();
        assertEquals(Integer.valueOf(4), converter.convert("Java"));
        assertThrows(NullPointerException.class, () -> converter.convert(null));
        assertThrows(NullPointerException.class, () -> new GenericRelationships.NumericValue<Integer>(null));
    }
}
