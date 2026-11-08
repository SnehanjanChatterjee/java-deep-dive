import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/** Separates preserving a timeline instant from preserving local clock fields. @author Snehanjan Chatterjee */
public final class ZoneConversionLab {
    /** Uses a fixed UTC instant and a known regional offset for both conversion policies. */
    public static void main(final String[] args) {
        final ZonedDateTime utc = Instant.parse("2024-01-01T12:00:00Z").atZone(ZoneId.of("UTC"));
        final ZoneId kolkata = ZoneId.of("Asia/Kolkata");
        final ZonedDateTime sameInstant = utc.withZoneSameInstant(kolkata);
        final ZonedDateTime sameLocal = utc.withZoneSameLocal(kolkata);
        System.out.println("same instant local=" + sameInstant.toLocalTime());
        System.out.println("same local instant=" + sameLocal.toInstant());
        System.out.println("same instant=" + utc.toInstant().equals(sameInstant.toInstant()));
        System.out.println("instant difference=" + Duration.between(sameLocal.toInstant(), utc.toInstant()).toMinutes());
    }
}
