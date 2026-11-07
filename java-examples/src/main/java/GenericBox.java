/** Keeps a stored value's type connected to its reads and writes. @author Snehanjan Chatterjee */
public final class GenericBox<T> {
    private T value;
    /** Stores a first value; null is permitted. */
    public GenericBox(final T value) { this.value = value; }
    /** Returns the value with its declared element type. */
    public T get() { return value; }
    /** Replaces the value with another value of the same element type. */
    public void set(final T value) { this.value = value; }
    /** Shows independent type arguments and a cast-free read. */
    public static void main(final String[] args) {
        final GenericBox<String> text = new GenericBox<>("first");
        text.set("second");
        final String value = text.get();
        final GenericBox<Integer> number = new GenericBox<>(7);
        System.out.println(value);
        System.out.println(number.get());
    }
}
