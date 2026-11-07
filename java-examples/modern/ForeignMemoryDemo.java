import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
/** Gives a native-memory segment an explicit arena lifetime. @author Snehanjan Chatterjee */
public final class ForeignMemoryDemo {
    /** Accesses a live segment and demonstrates rejection after closure. */
    public static void main(final String[] args) {
        final MemorySegment segment;
        try (final Arena arena = Arena.ofConfined()) {
            segment = arena.allocate(ValueLayout.JAVA_INT);
            segment.set(ValueLayout.JAVA_INT, 0, 42);
            System.out.println(segment.get(ValueLayout.JAVA_INT, 0));
        }
        try { segment.get(ValueLayout.JAVA_INT, 0); }
        catch (final IllegalStateException expected) { System.out.println("closed"); }
    }
}
