import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks strict date parsing at calendar boundaries. @author Snehanjan Chatterjee */
public final class DateTimeBasicsTest {
    @Test public void testValidLeapDate() { assertEquals(LocalDate.of(2024, 2, 29), DateTimeBasics.parseDate("2024-02-29")); }
    @Test public void testInvalidDatesAndNull() {
        assertThrows(DateTimeParseException.class, () -> DateTimeBasics.parseDate("2023-02-29"));
        assertThrows(DateTimeParseException.class, () -> DateTimeBasics.parseDate("2024-02-30"));
        assertThrows(DateTimeParseException.class, () -> DateTimeBasics.parseDate("2024-13-01"));
        assertThrows(NullPointerException.class, () -> DateTimeBasics.parseDate(null));
    }
}
