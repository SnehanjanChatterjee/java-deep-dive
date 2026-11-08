import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

/** Checks the named implementation and the three distinct default-selection rules. */
public final class InterfaceContractsLessonTest {
    @Test public void testFunctionalOperationAndMarkerType() {
        final InterfaceContractsLesson.TextOperation operation = new InterfaceContractsLesson.TaggedOperation();
        assertEquals("item:java", operation.apply("java"));
        assertEquals("item:", operation.apply(""));
        assertEquals("operation", operation.kind());
        assertTrue(operation instanceof InterfaceContractsLesson.Tagged);
        assertTrue(operation.equals(operation));
        assertEquals("one functional operation", InterfaceContractsLesson.TextOperation.description());
        assertThrows(NullPointerException.class, () -> operation.apply(null));
    }
    @Test public void testExplicitConflictAndMoreSpecificDefault() {
        assertEquals("left+right", new InterfaceContractsLesson.Combined().label());
        assertEquals("specific", new InterfaceContractsLesson.SpecificChoice().label());
    }
    @Test public void testConcreteClassMethodWinsOverDefaults() {
        final InterfaceContractsLesson.ClassChoice choice = new InterfaceContractsLesson.ClassChoice();
        final InterfaceContractsLesson.Left left = choice;
        final InterfaceContractsLesson.Right right = choice;
        assertEquals("base", left.label());
        assertEquals("base", right.label());
    }
}
