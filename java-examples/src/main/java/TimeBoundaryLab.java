import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

/** Exposes local-time ambiguity and decimal equality. @author Snehanjan Chatterjee */
public final class TimeBoundaryLab {
    /** Prints valid-offset counts for a historical gap and overlap. */
    public static void main(final String[] args) {
        final ZoneId zone = ZoneId.of("Europe/Paris");
        System.out.println(zone.getRules().getValidOffsets(LocalDateTime.of(2024, 3, 31, 2, 30)).size());
        System.out.println(zone.getRules().getValidOffsets(LocalDateTime.of(2024, 10, 27, 2, 30)).size());
        final BigDecimal first = new BigDecimal("1.0");
        final BigDecimal second = new BigDecimal("1.00");
        System.out.println("1.0 equals 1.00: " + first.equals(second));
        System.out.println("1.0 compares to 1.00: " + first.compareTo(second));
    }
}
