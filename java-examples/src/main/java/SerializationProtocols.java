import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/** Demonstrates stream handles and explicit reconstruction protocols with local bytes. @author Snehanjan Chatterjee */
public final class SerializationProtocols {
    /** A mutable value makes the output stream's identity cache observable. */
    public static final class MutableValue implements Serializable {
        private static final long serialVersionUID = 1L;
        public int value;
        /** Creates an ordinary mutable value for the handle-table trace. */
        public MutableValue(final int value) { this.value = value; }
    }

    /** A constructor-assigned final field is still omitted by transient. */
    public static final class TransientFinalValue implements Serializable {
        private static final long serialVersionUID = 1L;
        private final transient int value;
        /** Assigns a nonconstant final field through ordinary construction. */
        public TransientFinalValue(final int value) { this.value = value; }
        /** Reads the actual field, rather than an inlined constant expression. */
        public int value() { return value; }
    }

    /** Owns its external representation and validates it during reconstruction. */
    public static final class ExternalScore implements Externalizable {
        private static final long serialVersionUID = 1L;
        private static int constructionCount;
        private int value;

        /** Required public no-argument constructor used by Externalizable. */
        public ExternalScore() { constructionCount++; }

        /** Creates an ordinary valid score without incrementing the reconstruction counter. */
        public ExternalScore(final int value) { this.value = requireScore(value); }

        /** Returns the restored value. */
        public int value() { return value; }

        /** Writes a versioned format independently of default field serialization. */
        @Override public void writeExternal(final ObjectOutput output) throws IOException {
            output.writeInt(1);
            output.writeInt(value);
        }

        /** Rejects unknown formats and invalid values before assigning restored state. */
        @Override public void readExternal(final ObjectInput input) throws IOException {
            if (input.readInt() != 1) { throw new InvalidObjectException("unsupported format"); }
            final int restored = input.readInt();
            if (restored < 0 || restored > 100) { throw new InvalidObjectException("score outside 0..100"); }
            value = restored;
        }
    }

    /** Rebuilds a transient derived field rather than assuming the constructor runs. */
    public static final class CustomScore implements Serializable {
        private static final long serialVersionUID = 1L;
        private final int value;
        private transient String label;

        /** Establishes the ordinary construction invariant. */
        public CustomScore(final int value) {
            this.value = requireScore(value);
            label = "score:" + value;
        }

        /** Returns state reconstructed by the serialization hook. */
        public String label() { return label; }

        /** Uses the default persistent fields, validates them, then rebuilds derived state. */
        private void readObject(final ObjectInputStream input) throws IOException, ClassNotFoundException {
            input.defaultReadObject();
            if (value < 0 || value > 100) { throw new InvalidObjectException("score outside 0..100"); }
            label = "score:" + value;
        }
    }

    /** Replaces a reconstructed representation with one canonical object. */
    public static final class CanonicalToken implements Serializable {
        private static final long serialVersionUID = 1L;
        public static final CanonicalToken INSTANCE = new CanonicalToken();
        private CanonicalToken() { }

        /** Supplies the result returned to the containing graph. */
        private Object readResolve() { return INSTANCE; }
    }

    /** Copies one locally supplied object graph; this is not an untrusted-input decoder. */
    public static Object copy(final Object value) throws IOException, ClassNotFoundException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (final ObjectOutputStream output = new ObjectOutputStream(bytes)) { output.writeObject(value); }
        try (final ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            return input.readObject();
        }
    }

    /** Returns three roots read from one stream, with reset before the last write. */
    public static MutableValue[] repeatedRoots() throws IOException, ClassNotFoundException {
        final MutableValue original = new MutableValue(1);
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (final ObjectOutputStream output = new ObjectOutputStream(bytes)) {
            output.writeObject(original);
            original.value = 2;
            output.writeObject(original);
            output.reset();
            output.writeObject(original);
        }
        try (final ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            return new MutableValue[] {(MutableValue) input.readObject(), (MutableValue) input.readObject(), (MutableValue) input.readObject()};
        }
    }

    /** Enforces the source value's inclusive domain. */
    private static int requireScore(final int value) {
        if (value < 0 || value > 100) { throw new IllegalArgumentException("score outside 0..100"); }
        return value;
    }

    /** Prints identity, reset, constructor, transient-state and substitution observations. */
    public static void main(final String[] args) throws IOException, ClassNotFoundException {
        final MutableValue[] roots = repeatedRoots();
        System.out.println("same stream identity=" + (roots[0] == roots[1]));
        System.out.println("cached value=" + roots[1].value);
        System.out.println("after reset=" + roots[2].value);
        System.out.println("transient final=" + ((TransientFinalValue) copy(new TransientFinalValue(12))).value());
        ExternalScore.constructionCount = 0;
        final ExternalScore external = (ExternalScore) copy(new ExternalScore(7));
        System.out.println("external=" + external.value());
        System.out.println("constructor calls=" + ExternalScore.constructionCount);
        System.out.println("custom derived=" + ((CustomScore) copy(new CustomScore(9))).label());
        System.out.println("canonical identity=" + (copy(CanonicalToken.INSTANCE) == CanonicalToken.INSTANCE));
    }
}
