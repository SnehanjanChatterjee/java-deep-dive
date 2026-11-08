import java.util.Objects;

/** Models borrowing, sharing and privately owned parts with ordinary references. */
public final class ObjectRelationshipsLesson {
    static final class Book {
        private final String title;
        /** Constructs an independently usable book. */
        Book(final String title) { this.title = Objects.requireNonNull(title, "title"); }
    }
    static final class Reviewer {
        /** Borrows the book for one call without retaining it. */
        String review(final Book book) { return Objects.requireNonNull(book, "book").title; }
    }
    static final class ReadingGroup {
        private final Book book;
        /** Retains a caller-provided, potentially shared book. */
        ReadingGroup(final Book book) { this.book = Objects.requireNonNull(book, "book"); }
        /** Reports the shared book's title. */
        String title() { return book.title; }
    }
    static final class Notebook {
        private static final class Page { private String text = ""; }
        private final Page[] pages;
        /** Creates private parts; zero pages is valid. */
        Notebook(final int size) {
            if (size < 0) { throw new IllegalArgumentException("negative page count"); }
            pages = new Page[size];
            for (int index = 0; index < size; index++) { pages[index] = new Page(); }
        }
        /** Changes one owned part without exposing it. */
        void write(final int index, final String text) {
            pages[index].text = Objects.requireNonNull(text, "text");
        }
        /** Reads a page's immutable text value. */
        String read(final int index) { return pages[index].text; }
        /** Returns the number of owned pages. */
        int size() { return pages.length; }
    }
    /** Keeps the book reachable through an owner after clearing a local alias. */
    public static String throughRetainedOwner() {
        Book local = new Book("Retained");
        final ReadingGroup owner = new ReadingGroup(local);
        local = null;
        return owner.title();
    }
    /** Contrasts borrowing, shared aggregation and owned composition. */
    public static void main(final String[] args) {
        final Book book = new Book("Shared");
        System.out.println(new Reviewer().review(book));
        final ReadingGroup first = new ReadingGroup(book);
        final ReadingGroup second = new ReadingGroup(book);
        System.out.println(first.book == second.book);
        final Notebook notebook = new Notebook(2);
        notebook.write(0, "owned");
        System.out.println(notebook.read(0));
        System.out.println(notebook.size());
        System.out.println(throughRetainedOwner());
    }
}
