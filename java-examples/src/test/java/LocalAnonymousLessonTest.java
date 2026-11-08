import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks captured input and the two different receivers. */
public final class LocalAnonymousLessonTest {
    @Test public void testLocalAndAnonymousObjects() {
        final LocalAnonymousLesson outer = new LocalAnonymousLesson("Ada");
        assertEquals("Hello Ada", outer.local("Hello ").text());
        assertEquals("Hello Ada:inner", outer.anonymous("Hello ").text());
        assertEquals("Ada", outer.local("").text());
        assertThrows(NullPointerException.class, () -> new LocalAnonymousLesson(null));
        assertThrows(NullPointerException.class, () -> outer.local(null));
        assertThrows(NullPointerException.class, () -> outer.anonymous(null));
    }
}
