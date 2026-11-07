
/** Understand fixed-size arrays, immutable text and Unicode representation. @author Snehanjan Chatterjee */
public final class TextAndArrays {

    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        final String text = "A\uD83D\uDE00";
        System.out.println(text.length());
        System.out.println(text.codePointCount(0, text.length()));
        final StringBuilder builder = new StringBuilder("Java");
        final String original = builder.toString();
        builder.append(" 25");
        System.out.println(original);
        System.out.println(builder);
    }
}
