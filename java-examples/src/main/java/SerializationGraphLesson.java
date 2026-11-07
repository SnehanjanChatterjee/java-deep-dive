import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/** Serializes trusted in-memory data to observe identity sharing and transient fields. @author Snehanjan Chatterjee */
public final class SerializationGraphLesson {
    private static final class Address implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String city;
        /** Creates the shared nested value. */
        private Address(final String city) { this.city = city; }
    }
    private static final class Person implements Serializable {
        private static final long serialVersionUID = 1L;
        private final Address address;
        private final transient String session;
        /** Stores a shared address and nonpersistent session value. */
        private Person(final Address address, final String session) { this.address = address; this.session = session; }
    }

    /** Reconstructs one known graph from bytes created by this program. */
    private static Person[] copy(final Person[] values) throws IOException, ClassNotFoundException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (final ObjectOutputStream output = new ObjectOutputStream(bytes)) { output.writeObject(values); }
        try (final ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) { return (Person[]) input.readObject(); }
    }

    /** Distinguishes new identities from sharing preserved inside one reconstructed graph. */
    public static void main(final String[] args) throws IOException, ClassNotFoundException {
        final Address address = new Address("Kolkata");
        final Person[] original = {new Person(address, "one"), new Person(address, "two")};
        final Person[] restored = copy(original);
        System.out.println("same original=" + (original[0] == restored[0]));
        System.out.println("shared address=" + (restored[0].address == restored[1].address));
        System.out.println(restored[0].address.city);
        System.out.println("session=" + restored[0].session);
    }
}
