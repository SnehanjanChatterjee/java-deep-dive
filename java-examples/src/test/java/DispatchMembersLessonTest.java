import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/** Checks the member-selection rules without relying on printed output. */
public final class DispatchMembersLessonTest {
    @Test public void testOneObjectHasDistinctFieldsAndVirtualBehavior() {
        final DispatchMembersLesson.Child child = new DispatchMembersLesson.Child();
        final DispatchMembersLesson.Parent parent = child;
        assertSame(parent, child);
        assertEquals("parent field", parent.label);
        assertEquals("child field", child.label);
        assertEquals("child method", parent.describe());
        assertEquals("parent method", child.parentDescription());
        assertEquals("parent static", DispatchMembersLesson.Parent.kind());
        assertEquals("child static", DispatchMembersLesson.Child.kind());
    }
}
