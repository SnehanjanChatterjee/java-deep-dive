/** Implements set membership on the preceding hash-table exercise. @author Snehanjan Chatterjee */
public final class SimpleHashSet<T> {
    private static final Object PRESENT = new Object();
    private final MyHashMap<T, Object> entries = new MyHashMap<>();

    /** Adds one logical element, returning whether membership changed. */
    public boolean add(final T value) { return entries.put(value, PRESENT) == null; }

    /** Tests membership, including the supported null element. */
    public boolean contains(final T value) { return entries.containsKey(value); }

    /** Removes one element, returning whether it was present. */
    public boolean remove(final T value) { return entries.remove(value) == PRESENT; }

    /** Reports distinct logical elements. */
    public int size() { return entries.size(); }

    /** Demonstrates duplicate suppression and null membership. */
    public static void main(final String[] args) {
        final SimpleHashSet<String> values = new SimpleHashSet<>();
        System.out.println(values.add("java"));
        System.out.println(values.add(new String("java")));
        System.out.println(values.add(null));
        values.remove("java");
        System.out.println(values.size());
        System.out.println(values.contains("java"));
    }
}
