import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/** Fetches bounded UTF-8 demonstration text with explicit status and timeout policies. @author Snehanjan Chatterjee */
public final class HttpFetch {
    private static final int MAX_BYTES = 1_048_576;

    /** Fetches the URI supplied on the command line; network output is not deterministic. */
    public static void main(final String[] args) throws IOException, InterruptedException {
        if (args.length != 1) { throw new IllegalArgumentException("supply one http or https URI"); }
        final URI uri = URI.create(args[0]);
        if (!"https".equalsIgnoreCase(uri.getScheme()) && !"http".equalsIgnoreCase(uri.getScheme())) {
            throw new IllegalArgumentException("http or https required");
        }
        try (final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3)).build()) {
            final HttpRequest request = HttpRequest.newBuilder(uri).timeout(Duration.ofSeconds(5)).GET().build();
            final HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            try (final InputStream body = response.body()) {
                if (response.statusCode() != 200) { throw new IOException("HTTP status " + response.statusCode()); }
                final byte[] bytes = body.readNBytes(MAX_BYTES + 1);
                if (bytes.length > MAX_BYTES) { throw new IOException("response body exceeds limit"); }
                System.out.println(new String(bytes, StandardCharsets.UTF_8));
            }
        }
    }
}
