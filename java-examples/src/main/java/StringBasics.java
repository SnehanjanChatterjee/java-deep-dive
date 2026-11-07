/** Separates String values from mutable text construction. @author Snehanjan Chatterjee */
public final class StringBasics {
    /** Shows returned text, content comparison and builder ownership. */
    public static void main(final String[] args) {
        final String original = "  Java  ";
        final String cleaned = original.trim();
        System.out.println(original.length());
        System.out.println(cleaned);
        System.out.println(cleaned.equals(new String("Java")));
        final StringBuilder builder = new StringBuilder();
        for (int number = 1; number <= 3; number++) {
            if (number > 1) {
                builder.append(", ");
            }
            builder.append(number);
        }
        final String snapshot = builder.toString();
        builder.append("!");
        System.out.println(snapshot);
        System.out.println(builder);
    }
}
