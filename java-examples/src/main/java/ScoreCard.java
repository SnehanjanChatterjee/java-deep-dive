/** Gives two objects independent instance state. @author Snehanjan Chatterjee */
public final class ScoreCard {
    private int points;

    /** Adds one point to this particular card. */
    public void awardPoint() {
        points++;
    }

    /** Returns this card's current score. */
    public int points() {
        return points;
    }

    /** Creates two instances and changes only the first. */
    public static void main(final String[] args) {
        final ScoreCard first = new ScoreCard();
        final ScoreCard second = new ScoreCard();
        first.awardPoint();
        first.awardPoint();
        System.out.println(first.points());
        System.out.println(second.points());
        System.out.println(first == second);
    }
}
