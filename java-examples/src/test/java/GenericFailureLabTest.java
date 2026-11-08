import org.junit.Test;
import static org.junit.Assert.assertThrows;

/** Confirms intentionally unsafe examples fail at the stated boundary. @author Snehanjan Chatterjee */
public final class GenericFailureLabTest {
    @Test public void testUncheckedReadAndVarargsFailures() {
        assertThrows(ClassCastException.class, GenericFailureLab::rawRead);
        assertThrows(ClassCastException.class, GenericFailureLab::varargsRead);
    }
    @Test public void testCheckedWriteAndBackingBypassFailures() {
        assertThrows(ClassCastException.class, GenericFailureLab::checkedWrite);
        assertThrows(ClassCastException.class, GenericFailureLab::bypassRead);
    }
}
