/** Groups discrete input values with an ordinary switch statement. @author Snehanjan Chatterjee */
public final class SwitchBasics {
    /** Classifies ISO-style day numbers 1..7 while rejecting other inputs. */
    public static String dayType(final int day) {
        switch (day) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return "weekday";
            case 6:
            case 7:
                return "weekend";
            default:
                throw new IllegalArgumentException("day must be between 1 and 7");
        }
    }
    /** Shows two selected groups. */
    public static void main(final String[] args) {
        System.out.println(dayType(1));
        System.out.println(dayType(7));
    }
}
