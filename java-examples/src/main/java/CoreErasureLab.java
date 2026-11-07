import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;

/** Exposes the bridge generated to preserve an erased interface contract. @author Snehanjan Chatterjee */
public final class CoreErasureLab {
    interface Mapper<T> { T map(T value); }
    static final class StringMapper implements Mapper<String> {
        /** Implements the concrete String contract. */
        @Override public String map(final String value) { return value.toUpperCase(Locale.ROOT); }
    }
    /** Shows one bridge and shared runtime class identity across parameterizations. */
    public static void main(final String[] args) {
        final Mapper<String> mapper = new StringMapper();
        System.out.println(mapper.map("java"));
        int bridges = 0;
        for (final Method method : StringMapper.class.getDeclaredMethods()) {
            if (method.isBridge()) { bridges++; }
        }
        System.out.println(bridges);
        System.out.println(new ArrayList<String>().getClass() == new ArrayList<Integer>().getClass());
    }
}
