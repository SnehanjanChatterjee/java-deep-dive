import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Verifies the explicit lifetime and ownership policies of the example. */
public final class ObjectRelationshipsLessonTest {
    @Test public void testBorrowedAndRetainedReferences() {
        final ObjectRelationshipsLesson.Book book = new ObjectRelationshipsLesson.Book("Shared");
        assertEquals("Shared", new ObjectRelationshipsLesson.Reviewer().review(book));
        assertEquals("Shared", new ObjectRelationshipsLesson.ReadingGroup(book).title());
        assertEquals("Retained", ObjectRelationshipsLesson.throughRetainedOwner());
        assertThrows(NullPointerException.class, () -> new ObjectRelationshipsLesson.Book(null));
        assertThrows(NullPointerException.class, () -> new ObjectRelationshipsLesson.Reviewer().review(null));
        assertThrows(NullPointerException.class, () -> new ObjectRelationshipsLesson.ReadingGroup(null));
    }
    @Test public void testOwnedPagesAndInvalidInputs() {
        final ObjectRelationshipsLesson.Notebook first = new ObjectRelationshipsLesson.Notebook(2);
        final ObjectRelationshipsLesson.Notebook second = new ObjectRelationshipsLesson.Notebook(1);
        assertEquals("", first.read(0));
        first.write(0, "changed");
        assertEquals("changed", first.read(0));
        assertEquals("", second.read(0));
        assertEquals(0, new ObjectRelationshipsLesson.Notebook(0).size());
        assertThrows(IllegalArgumentException.class, () -> new ObjectRelationshipsLesson.Notebook(-1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> first.read(2));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> first.write(-1, "x"));
        assertThrows(NullPointerException.class, () -> first.write(0, null));
    }
}
