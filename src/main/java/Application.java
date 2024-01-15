import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.JsonResponse;
import data.SqlManager;
import data.Drinks;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class Application {

    public static final String url = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=margarita";

    public static void main(String[] args) throws IOException, InterruptedException {
        fetchJson(url);
    }

    private static void fetchJson(String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapperForResponse = new ObjectMapper();

        JsonResponse jsonResponse= mapperForResponse.readValue(response.body(), new TypeReference<JsonResponse>() {});
        List<Drinks> drinksList = jsonResponse.getDrinks();

        SqlManager sqlManager = new SqlManager();

        sqlManager.createDrinksTable();

        for (Drinks drinks : drinksList) {
            sqlManager.insertRecord(drinks);
        }
    }
}
