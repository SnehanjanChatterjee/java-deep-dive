/** Traces continue, an inner break and a labeled exit without arrays or helpers. */
public final class LoopControlLesson {
    /** Prints only the coordinates reached by the nested loops. */
    public static void main(final String[] args) {
        rows:
        for (int row = 1; row <= 3; row++) {
            for (int column = 1; column <= 4; column++) {
                if (column == 2) { continue; }
                if (row == 2 && column == 3) { break rows; }
                if (column == 4) { break; }
                System.out.println(row + ":" + column);
            }
        }
        System.out.println("after loops");
    }
}
