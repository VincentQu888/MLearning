package mlearning.infrastructure;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mlearning.entity.Dataset;
import mlearning.usecase.DatasetGateway;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Downloads and validates the pinned Iris dataset from the public OpenML API. */
public final class IrisDatasetGateway implements DatasetGateway {
    private static final URI METADATA = URI.create("https://www.openml.org/api/v1/json/data/61");
    private static final URI DOWNLOAD = URI.create("https://openml.org/data/v1/download/61/iris.arff");
    private static final List<String> FEATURES = List.of("Sepal length (cm)", "Sepal width (cm)", "Petal length (cm)", "Petal width (cm)");
    private static final List<String> LABELS = List.of("setosa", "versicolor", "virginica");
    private final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10))
            .followRedirects(HttpClient.Redirect.NORMAL).build();

    @Override public Dataset load(String source) {
        try {
            return switch (source == null ? "" : source) {
                case "openml" -> loadOpenMl();
                default -> throw new IllegalArgumentException("Choose openml as the dataset source.");
            };
        } catch (IOException exception) {
            throw new AccessException("Could not load Iris from " + source + ". Check your connection and retry.", exception);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new AccessException("Dataset retrieval was interrupted. Please retry.", exception);
        }
    }

    private Dataset loadOpenMl() throws IOException, InterruptedException {
        JsonNode description = new ObjectMapper().readTree(fetch(METADATA)).path("data_set_description");
        if (!"61".equals(description.path("id").asText()) || !"1".equals(description.path("version").asText())
                || !"61".equals(description.path("file_id").asText())) {
            throw new IOException("Unexpected OpenML dataset version.");
        }
        // Pinned URL, never an arbitrary URL supplied by a request or metadata response.
        return parse(fetch(DOWNLOAD));
    }

    private String fetch(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(uri).timeout(Duration.ofSeconds(20))
                .header("Accept", "application/json, text/plain, */*").GET().build();
        HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        try (InputStream body = response.body()) {
            if (response.statusCode() != 200) { throw new IOException("OpenML returned HTTP " + response.statusCode()); }
            byte[] bytes = body.readNBytes(200_001);
            if (bytes.length > 200_000) { throw new IOException("Dataset response is unexpectedly large."); }
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }

    /** Parses only the pinned five-column Iris ARFF schema. */
    private static Dataset parse(String text) throws IOException {
        List<double[]> rows = new ArrayList<>();
        List<Integer> targets = new ArrayList<>();
        boolean data = false;
        try {
            for (String line : text.split("\\R")) {
                line = line.trim();
                if (line.equalsIgnoreCase("@data")) { data = true; continue; }
                if (!data || line.isEmpty() || line.startsWith("%")) { continue; }
                String[] fields = line.split(",", -1);
                if (fields.length != 5) { throw new IllegalArgumentException("Expected five Iris columns."); }
                double[] values = new double[4];
                for (int i = 0; i < 4; i++) { values[i] = Double.parseDouble(fields[i].trim()); }
                String label = fields[4].trim().replace("'", "").replace("\"", "")
                        .toLowerCase(Locale.ROOT).replace("iris-", "");
                int target = LABELS.indexOf(label);
                if (target < 0) { throw new IllegalArgumentException("Unknown Iris class."); }
                rows.add(values);
                targets.add(target);
            }
            if (rows.size() != 150) { throw new IllegalArgumentException("Expected 150 Iris examples."); }
            for (int label = 0; label < LABELS.size(); label++) {
                final int current = label;
                if (targets.stream().filter(value -> value == current).count() != 50) {
                    throw new IllegalArgumentException("Expected 50 examples of each Iris class.");
                }
            }
            return new Dataset("Iris", "OpenML Iris #61 - version 1 (live)", FEATURES, LABELS, rows.toArray(double[][]::new),
                    targets.stream().mapToInt(Integer::intValue).toArray());
        } catch (IllegalArgumentException exception) {
            throw new IOException("Invalid Iris dataset: " + exception.getMessage(), exception);
        }
    }
}
