import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

/** Reads complete lines while keeping borrowed-input ownership explicit. */
public final class ConsoleInputLesson {
    /** Reads one required count from a borrowed reader without closing it. */
    public static int readCount(final BufferedReader input) throws IOException {
        final String line = Objects.requireNonNull(input, "input").readLine();
        if (line == null) { throw new IllegalArgumentException("missing count"); }
        final int value = Integer.parseInt(line.trim());
        if (value < 0) { throw new IllegalArgumentException("negative count"); }
        return value;
    }
    /** Demonstrates token reading leaving the rest of its current line. */
    public static String tokenThenLine(final String text) {
        try (Scanner scanner = new Scanner(Objects.requireNonNull(text, "text"))) {
            final int value = scanner.nextInt();
            final String rest = scanner.nextLine();
            final String name = scanner.nextLine();
            return value + ":remaining=[" + rest + "]:name=" + name;
        }
    }
    /** Uses fixed input by default; pass --stdin to borrow UTF-8 standard input. */
    public static void main(final String[] args) throws IOException {
        if (args.length == 1 && "--stdin".equals(args[0])) {
            final Reader reader = new InputStreamReader(System.in, StandardCharsets.UTF_8);
            final BufferedReader input = new BufferedReader(reader);
            try {
                System.out.println(readCount(input));
            } catch (final IllegalArgumentException failure) {
                System.out.println("invalid count");
            }
        } else {
            try (BufferedReader input = new BufferedReader(new StringReader(" 12 \n"))) {
                System.out.println(readCount(input));
            }
            System.out.println(tokenThenLine("12\nAda\n"));
        }
    }
}
