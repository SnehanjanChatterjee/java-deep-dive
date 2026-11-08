import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/** Sends controlled POST bodies to an owned loopback server; client lifecycle requires Java 21+. @author Snehanjan Chatterjee */
public final class HttpPostLab {
    /** Accepts only POST and at most 128 request bytes, closing every exchange. */
    private static void echo(final HttpExchange exchange) throws IOException {
        try {
            if (!"POST".equals(exchange.getRequestMethod())) { exchange.getResponseHeaders().set("Allow", "POST"); exchange.sendResponseHeaders(405, -1); return; }
            final byte[] body = exchange.getRequestBody().readNBytes(129);
            if (body.length > 128) { exchange.sendResponseHeaders(413, -1); return; }
            final byte[] prefix = "POST:".getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
            exchange.getResponseHeaders().set("X-Study-Mode", "echo");
            exchange.sendResponseHeaders(200, prefix.length + body.length);
            try (final OutputStream output = exchange.getResponseBody()) { output.write(prefix); output.write(body); }
        } finally { exchange.close(); }
    }

    /** Demonstrates a body publisher, response header, wrong method and size rejection. */
    public static void main(final String[] args) throws IOException, InterruptedException, URISyntaxException {
        final HttpServer server = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0), 0);
        server.createContext("/echo", HttpPostLab::echo);
        server.start();
        try (final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2))
                .followRedirects(HttpClient.Redirect.NEVER).build()) {
            final URI endpoint = new URI("http", null, server.getAddress().getAddress().getHostAddress(),
                server.getAddress().getPort(), "/echo", null, null);
            final HttpRequest request = HttpRequest.newBuilder(endpoint).timeout(Duration.ofSeconds(2))
                .header("Content-Type", "text/plain; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString("hello", StandardCharsets.UTF_8)).build();
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            System.out.println(response.statusCode() + ":" + response.body());
            System.out.println("header=" + response.headers().firstValue("X-Study-Mode").orElse("missing"));
            System.out.println("GET=" + client.send(HttpRequest.newBuilder(endpoint).GET().build(), HttpResponse.BodyHandlers.discarding()).statusCode());
            final HttpRequest oversized = HttpRequest.newBuilder(endpoint).POST(HttpRequest.BodyPublishers.ofByteArray(new byte[129])).build();
            System.out.println("oversize=" + client.send(oversized, HttpResponse.BodyHandlers.discarding()).statusCode());
        } finally { server.stop(0); }
    }
}
