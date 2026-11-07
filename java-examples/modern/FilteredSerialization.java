import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.InvalidObjectException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/** Applies an explicit graph/type policy before reconstructing a domain value. @author Snehanjan Chatterjee */
public final class FilteredSerialization {
    public static final class Score implements Serializable {
        @Serial private static final long serialVersionUID = 1L;
        private final int value;
        /** Establishes the ordinary construction invariant. */
        public Score(final int value) {
            if (value < 0 || value > 100) { throw new IllegalArgumentException("score outside 0..100"); }
            this.value = value;
        }
        /** Returns the validated score. */
        public int value() { return value; }
        /** Revalidates restored state because ordinary Serializable construction bypasses this constructor. */
        @Serial private void readObject(final ObjectInputStream input) throws IOException, ClassNotFoundException {
            input.defaultReadObject();
            if (value < 0 || value > 100) { throw new InvalidObjectException("score outside 0..100"); }
        }
    }

    /** Serializes caller-supplied local demonstration data. */
    static byte[] encode(final Serializable value) throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (final ObjectOutputStream output = new ObjectOutputStream(bytes)) { output.writeObject(value); }
        return bytes.toByteArray();
    }

    /** Accepts only a bounded Score graph and checks the top-level result. */
    static Score decode(final byte[] bytes) throws IOException, ClassNotFoundException {
        Objects.requireNonNull(bytes, "bytes");
        if (bytes.length > 4096) { throw new InvalidObjectException("input too large"); }
        try (final ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            input.setObjectInputFilter(info -> {
                if (info.depth() > 4 || info.references() > 20 || info.arrayLength() > 100
                        || info.streamBytes() > 4096) { return ObjectInputFilter.Status.REJECTED; }
                final Class<?> type = info.serialClass();
                if (type == null) { return ObjectInputFilter.Status.UNDECIDED; }
                return type == Score.class ? ObjectInputFilter.Status.ALLOWED : ObjectInputFilter.Status.REJECTED;
            });
            final Object result = input.readObject();
            if (!(result instanceof Score score)) { throw new InvalidObjectException("expected Score"); }
            return score;
        }
    }

    /** Shows accepted domain data and rejected unrelated serialized classes. */
    public static void main(final String[] args) throws IOException, ClassNotFoundException {
        System.out.println(decode(encode(new Score(42))).value());
        try {
            decode(encode(new ArrayList<String>()));
            throw new IllegalStateException("expected class rejection");
        } catch (final InvalidClassException expected) { System.out.println("rejected"); }
    }
}
