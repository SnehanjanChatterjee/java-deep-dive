import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/** Resolves ambiguous civil time using an explicit application policy. @author Snehanjan Chatterjee */
public final class CoreTimeResolution {
    public enum OverlapPolicy { EARLIER_OFFSET, LATER_OFFSET, REJECT }
    /** Rejects gaps and resolves overlaps without silently guessing a unique instant. */
    public static ZonedDateTime resolve(final LocalDateTime local, final ZoneId zone, final OverlapPolicy policy) {
        Objects.requireNonNull(local, "local");
        Objects.requireNonNull(zone, "zone");
        Objects.requireNonNull(policy, "policy");
        final List<ZoneOffset> offsets = zone.getRules().getValidOffsets(local);
        if (offsets.isEmpty()) { throw new DateTimeException("local time is in a gap"); }
        if (offsets.size() == 2 && policy == OverlapPolicy.REJECT) {
            throw new DateTimeException("overlap requires a chosen offset");
        }
        final int index = offsets.size() == 2 && policy == OverlapPolicy.LATER_OFFSET ? 1 : 0;
        return ZonedDateTime.ofStrict(local, offsets.get(index), zone);
    }
    /** Demonstrates two overlap instants and calendar versus elapsed-day arithmetic. */
    public static void main(final String[] args) {
        final ZoneId paris = ZoneId.of("Europe/Paris");
        final LocalDateTime overlap = LocalDateTime.of(2024, 10, 27, 2, 30);
        final ZonedDateTime early = resolve(overlap, paris, OverlapPolicy.EARLIER_OFFSET);
        final ZonedDateTime late = resolve(overlap, paris, OverlapPolicy.LATER_OFFSET);
        System.out.println(Duration.between(early.toInstant(), late.toInstant()).getSeconds());
        final ZonedDateTime noon = LocalDateTime.of(2024, 3, 30, 12, 0).atZone(paris);
        System.out.println(noon.plus(Period.ofDays(1)).toLocalTime());
        System.out.println(noon.plus(Duration.ofHours(24)).toLocalTime());
        try { resolve(LocalDateTime.of(2024, 3, 31, 2, 30), paris, OverlapPolicy.REJECT); }
        catch (final DateTimeException expected) { System.out.println("gap rejected"); }
    }
}
