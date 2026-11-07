import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

/** Declares metadata and reads its values without assuming automatic behavior. @author Snehanjan Chatterjee */
public final class AnnotationBasicsDemo {
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.METHOD})
    public @interface Label {
        String value();
        int priority() default 1;
    }
    @Label(value = "worker", priority = 2)
    public static class Worker {
        /** Supplies a method whose annotation has one defaulted element. */
        @Label("operation") public void work() { }
    }
    public static final class SpecializedWorker extends Worker { }

    /** Reads explicit/default elements and shows the default inheritance policy. */
    public static void main(final String[] args) throws NoSuchMethodException {
        final Label type = Worker.class.getAnnotation(Label.class);
        final Method method = Worker.class.getMethod("work");
        final Label action = method.getAnnotation(Label.class);
        System.out.println(type.value() + ":" + type.priority());
        System.out.println(action.value() + ":" + action.priority());
        System.out.println("inherited=" + SpecializedWorker.class.isAnnotationPresent(Label.class));
    }
}
