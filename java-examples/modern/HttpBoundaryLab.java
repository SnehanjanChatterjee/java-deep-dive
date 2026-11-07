import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/** Completes bounded-body and strict-decoding exercises against loopback endpoints. @author Snehanjan Chatterjee */
public final class HttpBoundaryLab {
    /** Writes a known local response and closes exchange ownership. */
    private static void respond(final HttpExchange exchange, final byte[] bytes) throws IOException {
        try {
            exchange.sendResponseHeaders(200, bytes.length);
            try (final OutputStream body = exchange.getResponseBody()) { body.write(bytes); }
        } finally { exchange.close(); }
    }
    /** Accepts status 200, bounds bytes and then strictly decodes the complete body. */
    private static String fetch(final HttpClient client, final URI uri) throws IOException, InterruptedException {
        final int limit = 8;
        final HttpRequest request = HttpRequest.newBuilder(uri).timeout(Duration.ofSeconds(2)).GET().build();
        final HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        try (final InputStream body = response.body()) {
            if (response.statusCode() != 200) { throw new IOException("unexpected status"); }
            final byte[] bytes = body.readNBytes(limit + 1);
            if (bytes.length > limit) { throw new IOException("body too large"); }
            return StandardCharsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT).decode(ByteBuffer.wrap(bytes)).toString();
        }
    }
    /** Separates an accepted body, an oversized body and malformed UTF-8. */
    public static void main(final String[] args) throws IOException, InterruptedException {
        final HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/ok", exchange -> respond(exchange, "hello".getBytes(StandardCharsets.UTF_8)));
        server.createContext("/large", exchange -> respond(exchange, new byte[10]));
        server.createContext("/invalid", exchange -> respond(exchange, new byte[] {(byte) 0xC3, 0x28}));
        server.start();
        try (final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2)).build()) {
            final String base = "http://127.0.0.1:" + server.getAddress().getPort();
            System.out.println(fetch(client, URI.create(base + "/ok")));
            try { fetch(client, URI.create(base + "/large")); throw new IllegalStateException("expected size failure"); }
            catch (final IOException expected) {
                if (!"body too large".equals(expected.getMessage())) { throw expected; }
                System.out.println("size rejected");
            }
            try { fetch(client, URI.create(base + "/invalid")); throw new IllegalStateException("expected decoding failure"); }
            catch (final CharacterCodingException expected) { System.out.println("encoding rejected"); }
        } finally { server.stop(0); }
    }
}
