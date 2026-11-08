import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ReadOnlyBufferException;

/** Exposes shared bytes and independent cursor state without a network dependency. @author Snehanjan Chatterjee */
public final class BufferViewsLab {
    /** Prints relative/absolute accesses, mark/reset and shared-view observations. */
    public static void main(final String[] args) {
        final ByteBuffer source = ByteBuffer.allocate(8);
        source.put(new byte[] {10, 20, 30, 40});
        source.flip();
        System.out.println("absolute=" + source.get(1) + ", position=" + source.position());
        System.out.println("relative=" + source.get() + ", position=" + source.position());
        source.mark();
        source.get();
        source.reset();
        System.out.println("reset position=" + source.position());
        source.order(ByteOrder.LITTLE_ENDIAN);
        final ByteBuffer duplicate = source.duplicate();
        System.out.println("view orders=" + source.order() + "/" + duplicate.order());
        duplicate.get();
        System.out.println("cursors=" + source.position() + "/" + duplicate.position());
        final ByteBuffer slice = source.slice();
        slice.put(0, (byte) 99);
        System.out.println("slice shared=" + source.get(1));
        final ByteBuffer readOnly = source.asReadOnlyBuffer();
        source.put(1, (byte) 88);
        System.out.println("readonly observes=" + readOnly.get(1));
        try { readOnly.put(1, (byte) 0); }
        catch (final ReadOnlyBufferException expected) { System.out.println("readonly write rejected"); }
        final ByteBuffer endian = ByteBuffer.wrap(new byte[] {1, 2, 3, 4});
        System.out.println("big endian=" + endian.getInt(0));
        endian.order(ByteOrder.LITTLE_ENDIAN);
        System.out.println("little endian=" + endian.getInt(0));
    }
}
