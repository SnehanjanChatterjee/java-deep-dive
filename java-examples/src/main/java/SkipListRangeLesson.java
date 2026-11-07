import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/** Distinguishes a backed sorted range view from individual entry snapshots. @author Snehanjan Chatterjee */
public final class SkipListRangeLesson {
    /** Uses ordering and atomic endpoint removal without a whole-map snapshot claim. */
    public static void main(final String[] args) {
        final ConcurrentSkipListMap<Integer, String> jobs = new ConcurrentSkipListMap<>();
        jobs.put(10, "first"); jobs.put(30, "third");
        final NavigableMap<Integer, String> upcoming = jobs.subMap(10, true, 30, false);
        jobs.put(20, "second");
        System.out.println(upcoming);
        System.out.println(jobs.floorKey(25));
        System.out.println(jobs.pollFirstEntry());
        System.out.println(upcoming);
    }
}
