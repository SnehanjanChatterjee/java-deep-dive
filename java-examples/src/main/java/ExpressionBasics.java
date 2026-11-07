/** Uses declarations, assignment and boolean expressions before numeric edge cases. @author Snehanjan Chatterjee */
public final class ExpressionBasics {
    /** Computes a small purchase and then changes one local variable. */
    public static void main(final String[] args) {
        int quantity = 3;
        final int unitPrice = 5;
        final int firstTotal = quantity * unitPrice;
        quantity = quantity + 1;
        final int nextTotal = quantity * unitPrice;
        final boolean withinBudget = nextTotal <= 20;
        System.out.println(firstTotal);
        System.out.println(nextTotal);
        System.out.println(withinBudget);
    }
}
