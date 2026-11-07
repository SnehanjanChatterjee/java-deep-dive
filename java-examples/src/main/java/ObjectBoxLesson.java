/** Exhibits the cast failure that generic type checking prevents. @author Snehanjan Chatterjee */
public final class ObjectBoxLesson {
    private Object value;
    /** Accepts any reference value, losing its specific type in the API. */
    public void set(final Object value) { this.value = value; }
    /** Returns only the common Object contract. */
    public Object get() { return value; }
    /** Shows one successful cast and a later incompatible write. */
    public static void main(final String[] args) {
        final ObjectBoxLesson box = new ObjectBoxLesson();
        box.set("Java");
        System.out.println(((String) box.get()).length());
        box.set(Integer.valueOf(7));
        try {
            final String text = (String) box.get();
            System.out.println(text);
        } catch (final ClassCastException failure) {
            System.out.println("wrong value type");
        }
    }
}
