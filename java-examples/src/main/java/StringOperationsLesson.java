/** Exercises String search, slicing, comparison and literal replacement contracts. */
public final class StringOperationsLesson {
    /** Prints deterministic results without depending on interning as equality. */
    public static void main(final String[] args) {
        final String text = "banana";
        System.out.println(text.charAt(0));
        System.out.println(text.indexOf("ana"));
        System.out.println(text.indexOf("ana", 2));
        System.out.println(text.lastIndexOf("ana"));
        System.out.println(text.substring(1, 4));
        System.out.println("[" + text.substring(3, 3) + "]");
        System.out.println(text.indexOf("pear"));
        System.out.println("a.b".replace(".", "-"));
        System.out.println("cat".compareTo("car"));
        System.out.println("car".compareTo("cart"));
        final String literal = "Java";
        final String separate = new String(literal);
        System.out.println(literal == separate);
        System.out.println(literal.equals(separate));
        System.out.println(literal == separate.intern());
    }
}
