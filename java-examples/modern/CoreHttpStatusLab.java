import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/** Uses only a temporary loopback server to distinguish HTTP status from transport failure. @author Snehanjan Chatterjee */
public final class CoreHttpStatusLab {
    /** Sends one fixed small response and closes exchange ownership. */
    private static void respond(final HttpExchange exchange, final int status, final String text) throws IOException {
        try {
            final byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(status, bytes.length);
            try (final OutputStream body = exchange.getResponseBody()) { body.write(bytes); }
        } finally { exchange.close(); }
    }
    /** Applies an application status policy after receiving an ordinary response. */
    private static String requireSuccess(final HttpResponse<String> response) throws IOException {
        if (response.statusCode() != 200) { throw new IOException("HTTP status " + response.statusCode()); }
        return response.body();
    }
    /** Demonstrates two statuses without contacting an external site. */
    public static void main(final String[] args) throws IOException, InterruptedException {
        final HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/ok", exchange -> respond(exchange, 200, "hello"));
        server.createContext("/missing", exchange -> respond(exchange, 404, "missing"));
        server.start();
        try (final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2)).build()) {
            final String base = "http://127.0.0.1:" + server.getAddress().getPort();
            final HttpRequest ok = HttpRequest.newBuilder(URI.create(base + "/ok")).timeout(Duration.ofSeconds(2)).GET().build();
            final HttpResponse<String> response = client.send(ok, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            System.out.println(response.statusCode() + ":" + requireSuccess(response));
            final HttpRequest missing = HttpRequest.newBuilder(URI.create(base + "/missing")).timeout(Duration.ofSeconds(2)).GET().build();
            final HttpResponse<String> failedStatus = client.send(missing, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            System.out.println(failedStatus.statusCode());
            try { requireSuccess(failedStatus); throw new IllegalStateException("expected status rejection"); }
            catch (final IOException expected) { System.out.println("status rejected"); }
        } finally { server.stop(0); }
    }
}
