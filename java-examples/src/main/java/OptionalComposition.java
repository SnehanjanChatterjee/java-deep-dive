import java.util.Objects;
import java.util.Optional;

/** Composes optional lookups while keeping malformed data a separate failure. @author Snehanjan Chatterjee */
public final class OptionalComposition {
    public static final class Account {
        private final String address;
        /** Stores the nullable address used by this small lookup fixture. */
        public Account(final String address) { this.address = address; }
        /** Exposes address absence rather than returning a null Optional. */
        public Optional<String> address() { return Optional.ofNullable(address); }
    }
    /** Finds one account under a small declared fixture contract. */
    public static Optional<Account> find(final String key) {
        Objects.requireNonNull(key, "key");
        if (key.equals("known")) { return Optional.of(new Account("Main Street")); }
        if (key.equals("no-address")) { return Optional.of(new Account(null)); }
        return Optional.empty();
    }
    /** Composes the account and address presence dimensions into one result. */
    public static Optional<String> findAddress(final String key) { return find(key).flatMap(Account::address); }
    /** Shows the nested type produced by map and two deliberately different null policies. */
    public static void main(final String[] args) {
        final Optional<Optional<String>> nested = find("known").map(Account::address);
        System.out.println(nested.isPresent() + "/" + nested.get().isPresent());
        System.out.println(findAddress("known").orElse("none"));
        System.out.println(findAddress("no-address").orElse("none"));
        System.out.println(findAddress("missing").orElse("none"));
        System.out.println("map null becomes empty=" + !Optional.of("x").map(value -> null).isPresent());
        try { Optional.of("x").flatMap(value -> null); throw new IllegalStateException("expected null Optional failure"); }
        catch (final NullPointerException expected) { System.out.println("flatMap null rejected"); }
        try { Optional.empty().map(null); throw new IllegalStateException("expected null function failure"); }
        catch (final NullPointerException expected) { System.out.println("null function rejected even for empty"); }
    }
}
