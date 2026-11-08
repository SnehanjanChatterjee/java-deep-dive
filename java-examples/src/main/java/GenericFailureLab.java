import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Deliberately unsafe examples locate the runtime consequence of discarded type evidence. @author Snehanjan Chatterjee */
public final class GenericFailureLab {
    /** Demonstrates a raw write accepted before a later typed read fails. */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static String rawRead() {
        final List<String> typed = new ArrayList<>();
        final List raw = typed;
        raw.add(Integer.valueOf(7));
        return typed.get(0);
    }
    /** Intentionally pollutes a generic varargs array; this is not SafeVarargs. */
    @SuppressWarnings({"unchecked", "varargs"})
    private static String polluted(final List<String>... inputs) {
        final Object[] alias = inputs;
        alias[0] = Collections.singletonList(Integer.valueOf(7));
        return inputs[0].get(0);
    }
    /** Isolates the unchecked generic-array creation used only by this failure demonstration. */
    @SuppressWarnings("unchecked")
    public static String varargsRead() { return polluted(Arrays.asList("safe")); }
    /** Makes one legacy raw call so its write can be compared through different wrappers. */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void writeLegacy(final List values) { values.add(Integer.valueOf(7)); }
    /** A checked view rejects a wrong runtime class at the write boundary. */
    public static void checkedWrite() {
        final List<String> backing = new ArrayList<>();
        writeLegacy(Collections.checkedList(backing, String.class));
    }
    /** A checked wrapper cannot protect writes made through a different raw backing alias. */
    public static String bypassRead() {
        final List<String> backing = new ArrayList<>();
        final List<String> checked = Collections.checkedList(backing, String.class);
        writeLegacy(backing);
        return checked.get(0);
    }
    /** Prints the failure stage instead of pretending unchecked code restored type safety. */
    public static void main(final String[] args) {
        try { rawRead(); throw new IllegalStateException("expected typed-read failure"); }
        catch (final ClassCastException expected) { System.out.println("raw write: later read rejected"); }
        try { varargsRead(); throw new IllegalStateException("expected varargs failure"); }
        catch (final ClassCastException expected) { System.out.println("varargs alias: later read rejected"); }
        try { checkedWrite(); throw new IllegalStateException("expected checked-write failure"); }
        catch (final ClassCastException expected) { System.out.println("checked view: write rejected"); }
        try { bypassRead(); throw new IllegalStateException("expected bypass failure"); }
        catch (final ClassCastException expected) { System.out.println("backing alias: later read rejected"); }
    }
}
