/** Uses only statements in main to introduce branches and loop execution. @author Snehanjan Chatterjee */
public final class BranchAndLoopLesson {
    /** Runs a branch, a counted sum, a countdown and one post-test iteration. */
    public static void main(final String[] args) {
        final int score = 76;
        if (score >= 50) { System.out.println("pass"); }
        else { System.out.println("retry"); }
        int total = 0;
        for (int number = 1; number <= 4; number++) { total += number; }
        System.out.println(total);
        int remaining = 3;
        while (remaining > 0) { System.out.println(remaining); remaining--; }
        int attempts = 0;
        do { attempts++; } while (attempts < 1);
        System.out.println(attempts);
    }
}
