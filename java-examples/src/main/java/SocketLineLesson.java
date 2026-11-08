import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/** Uses two processes and one bounded UTF-8 line to introduce blocking TCP sockets. @author Snehanjan Chatterjee */
public final class SocketLineLesson {
    /** Reads one LF-terminated frame of at most 64 UTF-16 units from a borrowed reader. */
    static String readFrame(final Reader reader) throws IOException {
        Objects.requireNonNull(reader, "reader");
        final StringBuilder value = new StringBuilder();
        for (int next; (next = reader.read()) != -1;) {
            if (next == '\n') { return value.toString(); }
            if (value.length() == 64) { throw new IOException("line exceeds 64 units"); }
            value.append((char) next);
        }
        throw new EOFException("connection ended before LF");
    }

    /** Writes one validated line and flushes without closing the borrowed writer. */
    static void writeFrame(final Writer writer, final String value) throws IOException {
        Objects.requireNonNull(writer, "writer");
        Objects.requireNonNull(value, "value");
        if (value.length() > 64 || value.indexOf('\n') >= 0) { throw new IllegalArgumentException("invalid line"); }
        writer.write(value);
        writer.write('\n');
        writer.flush();
    }

    /** Adapts socket bytes to strict UTF-8 characters; socket ownership remains with the caller. */
    private static Reader reader(final Socket socket) throws IOException {
        return new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8.newDecoder()
            .onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT));
    }

    /** Encodes strictly without transferring ownership of the socket. */
    private static Writer writer(final Socket socket) throws IOException {
        return new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8.newEncoder()
            .onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT));
    }

    /** Accepts one loopback connection, echoes one bounded frame, then closes both sockets. */
    private static void serve() throws IOException {
        try (final ServerSocket listener = new ServerSocket(0, 1, InetAddress.getLoopbackAddress())) {
            listener.setSoTimeout(30_000);
            System.out.println("port=" + listener.getLocalPort());
            try (final Socket connection = listener.accept()) {
                connection.setSoTimeout(3_000);
                final String request = readFrame(reader(connection));
                writeFrame(writer(connection), request);
            }
            System.out.println("served one request");
        }
    }

    /** Connects to the locally supplied port and completes one request/reply. */
    private static void request(final int port) throws IOException {
        if (port < 1 || port > 65535) { throw new IllegalArgumentException("invalid port"); }
        try (final Socket connection = new Socket()) {
            connection.connect(new InetSocketAddress(InetAddress.getLoopbackAddress(), port), 2_000);
            connection.setSoTimeout(3_000);
            writeFrame(writer(connection), "hello");
            System.out.println("reply=" + readFrame(reader(connection)));
        }
    }

    /** Runs the server or client in a separate process, without introducing thread APIs. */
    public static void main(final String[] args) throws IOException {
        if (args.length == 1 && "server".equals(args[0])) { serve(); }
        else if (args.length == 2 && "client".equals(args[0])) { request(Integer.parseInt(args[1])); }
        else { throw new IllegalArgumentException("usage: SocketLineLesson server | client <port>"); }
    }
}
