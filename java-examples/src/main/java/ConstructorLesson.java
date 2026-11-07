import java.util.Objects;

/** Establishes an invariant and contrasts class and instance state. @author Snehanjan Chatterjee */
public final class ConstructorLesson {
    private static int created;
    private final String name;
    private int points;

    /** Creates a named card with an initial score of zero. */
    public ConstructorLesson(final String name) {
        this(name, 0);
    }

    /** Creates a named card with a nonnegative initial score. */
    public ConstructorLesson(final String name, final int points) {
        this.name = Objects.requireNonNull(name, "name");
        if (points < 0) {
            throw new IllegalArgumentException("points must be nonnegative");
        }
        this.points = points;
        created++;
    }

    /** Changes the score while preserving the nonnegative invariant. */
    public void award(final int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount must be nonnegative");
        }
        points = Math.addExact(points, amount);
    }

    /** Returns the private instance name. */
    public String name() { return name; }
    /** Returns the private instance score. */
    public int points() { return points; }
    /** Reports successful constructions in this single-threaded teaching example. */
    public static int created() { return created; }

    /** Shows constructor chaining, independent fields and one shared class field. */
    public static void main(final String[] args) {
        final ConstructorLesson ada = new ConstructorLesson("Ada");
        final ConstructorLesson lin = new ConstructorLesson("Lin", 3);
        ada.award(2);
        System.out.println(ada.name() + ":" + ada.points());
        System.out.println(lin.name() + ":" + lin.points());
        System.out.println(ConstructorLesson.created());
    }
}
