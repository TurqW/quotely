package turq.quotely;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class QuoteGenerator {
    private static final String ENDPOINT = "http://api.forismatic.com/api/1.0/?";
    private static final int MAX_RETRY = 5;

    // in a production system built in an actual framework, this would be autowired in some way
    private final HttpClient client;

    public QuoteGenerator(HttpClient client) {
        this.client = client;
    }

    public String getQuote(final String language) {
        Map<String, String> params = new HashMap<>();
        params.put("method", "getQuote");
        params.put("format", "text");
        params.put("lang", language);

        URI uri = URI.create(ENDPOINT + paramsToString(params));
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        Future<String> result = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenComposeAsync(resp -> retryIfNeeded(request, resp, 0));

        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    private String paramsToString(final Map<String, String> params) {
        return params.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
    }

    private CompletableFuture<String> retryIfNeeded(
            final HttpRequest request,
            final HttpResponse<String> response,
            final int count) {
        if (count >= MAX_RETRY) {
            throw new RuntimeException(response.body());
        }
        if (response.statusCode() == 200 && !response.body().startsWith("Error")) {
            return CompletableFuture.completedFuture(response.body());
        } else {
            // TODO: exponential backoff
            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenComposeAsync(resp -> retryIfNeeded(request, resp, count + 1));
        }

    }
}
