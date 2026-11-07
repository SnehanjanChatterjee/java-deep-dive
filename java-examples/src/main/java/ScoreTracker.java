import java.util.HashMap;
import java.util.Map;

/** Demonstrates insertion, replacement and lookup. @author Snehanjan Chatterjee */
public class ScoreTracker {
    public static void main(final String[] args) {
        final Map<String, Integer> scores = new HashMap<>();
        scores.put("S101", 92);
        scores.put("S102", 88);
        scores.put("S101", 97); // replaces the previous score

        System.out.println(scores.get("S101"));
        System.out.println(scores.containsKey("S102"));
        System.out.println(scores.size());
    }
}
