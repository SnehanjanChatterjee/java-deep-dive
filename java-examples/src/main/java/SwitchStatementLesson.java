/** Classifies a value inside main before introducing helper-method returns. */
public final class SwitchStatementLesson {
    /** Assigns one message and continues after the switch. */
    public static void main(final String[] args) {
        final int day = 7;
        final String kind;
        switch (day) {
            case 1: case 2: case 3: case 4: case 5:
                kind = "weekday";
                break;
            case 6: case 7:
                kind = "weekend";
                break;
            default:
                kind = "invalid day";
                break;
        }
        System.out.println(kind);
        System.out.println("after switch");
    }
}
