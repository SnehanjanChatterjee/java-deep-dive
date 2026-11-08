import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks the shared algorithm and each concrete representation's invariant. */
public final class AbstractTemplateLessonTest {
    @Test public void testSharedFormattingUsesConcreteContent() {
        final AbstractTemplateLesson.Form text = new AbstractTemplateLesson.TextForm("note", "hello");
        final AbstractTemplateLesson.Form count = new AbstractTemplateLesson.CountForm("items", 3);
        assertEquals("note:hello", text.format());
        assertEquals("items:3", count.format());
        assertEquals(":", new AbstractTemplateLesson.TextForm("", "").format());
        assertEquals("items:0", new AbstractTemplateLesson.CountForm("items", 0).format());
    }
    @Test public void testRequiredStateAndNegativeCount() {
        assertThrows(NullPointerException.class, () -> new AbstractTemplateLesson.TextForm(null, "hello"));
        assertThrows(NullPointerException.class, () -> new AbstractTemplateLesson.TextForm("note", null));
        assertThrows(NullPointerException.class, () -> new AbstractTemplateLesson.CountForm(null, 1));
        assertThrows(IllegalArgumentException.class, () -> new AbstractTemplateLesson.CountForm("items", -1));
    }
}
