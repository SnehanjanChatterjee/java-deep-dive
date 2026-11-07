import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import static java.lang.foreign.MemoryLayout.PathElement.groupElement;

/** Separates named layout offsets from the lifetime of allocated native storage. @author Snehanjan Chatterjee */
public final class ForeignLayoutLesson {
    /** Allocates a two-int layout, uses named offsets and observes closure. */
    public static void main(final String[] args) {
        final MemoryLayout point = MemoryLayout.structLayout(ValueLayout.JAVA_INT.withName("x"), ValueLayout.JAVA_INT.withName("y"));
        final long x = point.byteOffset(groupElement("x")), y = point.byteOffset(groupElement("y"));
        final MemorySegment segment;
        try (final Arena arena = Arena.ofConfined()) {
            segment = arena.allocate(point);
            segment.set(ValueLayout.JAVA_INT, x, 3); segment.set(ValueLayout.JAVA_INT, y, 4);
            System.out.println(x + ":" + y);
            System.out.println(segment.get(ValueLayout.JAVA_INT, x) + segment.get(ValueLayout.JAVA_INT, y));
        }
        try { segment.get(ValueLayout.JAVA_INT, x); }
        catch (final IllegalStateException expected) { System.out.println("closed"); return; }
        throw new IllegalStateException("closed access succeeded");
    }
}
