import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/** Demonstrates deep ownership boundaries for mutable components. @author Snehanjan Chatterjee */
public final class ImmutableSchedule {
    private final List<Date> dates;

    /** Takes a detached snapshot of the supplied dates. */
    public ImmutableSchedule(final List<Date> dates) {
        this.dates = copyDates(dates);
    }

    /** Returns a detached unmodifiable list whose elements may be changed safely. */
    public List<Date> dates() {
        return copyDates(dates);
    }

    /** Copies the container and every mutable element. */
    private static List<Date> copyDates(final List<Date> source) {
        Objects.requireNonNull(source, "dates");
        final List<Date> copy = new ArrayList<>(source.size());
        for (final Date date : source) {
            copy.add(new Date(Objects.requireNonNull(date, "date").getTime()));
        }
        return Collections.unmodifiableList(copy);
    }

    /** Compares independently constructed snapshots by their date values. */
    @Override public boolean equals(final Object other) {
        return this == other || other instanceof ImmutableSchedule
                && dates.equals(((ImmutableSchedule) other).dates);
    }

    /** Hashes the same state used for equality. */
    @Override public int hashCode() {
        return dates.hashCode();
    }

    /** Shows that neither input nor accessor mutation changes the snapshot. */
    public static void main(final String[] args) {
        final Date input = new Date(1000L);
        final ImmutableSchedule schedule = new ImmutableSchedule(Collections.singletonList(input));
        input.setTime(2000L);
        System.out.println(schedule.dates().get(0).getTime());
        schedule.dates().get(0).setTime(3000L);
        System.out.println(schedule.dates().get(0).getTime());
        System.out.println(schedule.equals(new ImmutableSchedule(Collections.singletonList(new Date(1000L)))));
    }
}
