import java.util.Objects;

/** Gives class, interface and bounded parameters separate jobs. @author Snehanjan Chatterjee */
public final class GenericRelationships {
    public static final class Entry<K, V> {
        private final K key;
        private final V value;
        /** Preserves two independent reference-type relationships; null components are allowed. */
        public Entry(final K key, final V value) { this.key = key; this.value = value; }
        /** Returns the declared key type. */
        public K key() { return key; }
        /** Returns the independently declared value type. */
        public V value() { return value; }
    }
    public interface Converter<F, T> { T convert(F value); }
    public static final class Length implements Converter<String, Integer> {
        /** Implements the String-to-Integer contract without a caller cast. */
        @Override public Integer convert(final String value) {
            return Objects.requireNonNull(value, "value").length();
        }
    }
    public static final class NumericValue<N extends Number> {
        private final N value;
        /** Establishes a non-null value with Number's readable operations. */
        public NumericValue(final N value) { this.value = Objects.requireNonNull(value, "value"); }
        /** Returns the original precise static type. */
        public N value() { return value; }
        /** Uses an operation made available by the upper bound; conversion may be approximate. */
        public double asDouble() { return value.doubleValue(); }
    }
    /** Uses different type arguments in three independently typed declarations. */
    public static void main(final String[] args) {
        final Entry<String, Integer> score = new Entry<>("Ada", 7);
        final String name = score.key();
        final int points = score.value();
        System.out.println(name + ":" + points);
        final Converter<String, Integer> length = new Length();
        System.out.println(length.convert("Java"));
        final NumericValue<Double> decimal = new NumericValue<>(7.5);
        System.out.println(decimal.asDouble());
    }
}
