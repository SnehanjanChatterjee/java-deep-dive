import java.nio.charset.StandardCharsets;

/** Compares copied String inputs with mutable builder operations and immutable snapshots. @author Snehanjan Chatterjee */
public final class StringConstructionLab {
    /** Prints constructor, mutation, snapshot and case-insensitive comparison observations. */
    public static void main(final String[] args) {
        final char[] input = {'J', 'a', 'v', 'a'};
        final String text = new String(input);
        input[0] = 'L';
        System.out.println("from chars=" + text);
        System.out.println("slice=" + new String(input, 1, 2));
        final byte[] encoded = "text".getBytes(StandardCharsets.UTF_8);
        System.out.println("from bytes=" + new String(encoded, StandardCharsets.UTF_8));
        final StringBuilder builder = new StringBuilder(32);
        System.out.println("length=" + builder.length() + ", reserved=" + (builder.capacity() >= 32));
        builder.append("ava").insert(0, 'J');
        builder.replace(0, 1, "L");
        builder.delete(1, 2);
        builder.setCharAt(1, 'i');
        builder.append('!');
        final String snapshot = builder.toString();
        System.out.println("built=" + snapshot);
        builder.reverse();
        builder.setLength(2);
        System.out.println("builder=" + builder);
        builder.setLength(4);
        System.out.println("zero fill=" + (builder.charAt(2) == 0 && builder.charAt(3) == 0));
        System.out.println("snapshot=" + snapshot);
        System.out.println("case ordering=" + "JAVA".compareToIgnoreCase("java"));
        System.out.println("case equal=" + "JAVA".equalsIgnoreCase("java"));
        final StringBuffer buffer = new StringBuffer("x");
        buffer.append('y');
        System.out.println("buffer=" + buffer);
    }
}
