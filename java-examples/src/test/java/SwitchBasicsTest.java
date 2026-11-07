import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Covers each switch group and invalid domain boundaries. @author Snehanjan Chatterjee */
public final class SwitchBasicsTest {
    @Test public void testDayGroups() {
        for (int day = 1; day <= 5; day++) { assertEquals("weekday", SwitchBasics.dayType(day)); }
        assertEquals("weekend", SwitchBasics.dayType(6));
        assertEquals("weekend", SwitchBasics.dayType(7));
    }
    @Test public void testRejectInvalidDays() {
        assertThrows(IllegalArgumentException.class, () -> SwitchBasics.dayType(0));
        assertThrows(IllegalArgumentException.class, () -> SwitchBasics.dayType(8));
    }
}
