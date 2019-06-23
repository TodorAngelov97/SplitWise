package bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler;

import bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler.rate.DollarRate;
import bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler.rate.EuroRate;
import bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler.rate.LevRate;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

public class RateHandler {
    private String currentCurrency;
    private String wantedCurrency;

    public RateHandler(String fromCurrency, String toCurrency) {
        this.currentCurrency = fromCurrency;
        this.wantedCurrency = toCurrency;
    }

    private String getJson() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String req = String.format("https://api.exchangeratesapi.io/latest?base=%s&symbols=%s", currentCurrency,
                wantedCurrency);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(req))
                .build();
        return client.send(request, BodyHandlers.ofString()).body();
    }


    // new interface
    public double getRate() {

        Gson gson = new Gson();
        try {
            if (wantedCurrency.equals("EUR")) {
                EuroRate rate = gson.fromJson(getJson(), EuroRate.class);
                return rate.getRate();

            } else if (wantedCurrency.equals("USD")) {
                DollarRate rate = gson.fromJson(getJson(), DollarRate.class);
                return rate.getRate();
            }
            LevRate rate = gson.fromJson(getJson(), LevRate.class);
            return rate.getRate();
        } catch (JsonSyntaxException e) {
            System.err.println("Exception thrown by Json: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Exception thrown by Json: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Exception thrown by Json: " + e.getMessage());

        }
        return 0;
    }

    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();
        String req = String.format("https://api.exchangeratesapi.io/latest?base=%s&symbols=%s", "EUR", "USD");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(req))
                .build();
        try {
            Gson gson = new Gson();
            DollarRate rate = gson.fromJson(client.send(request, BodyHandlers.ofString()).body(), DollarRate.class);
            System.out.println(rate.getRate());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RateHandler rater = new RateHandler("EUR", "USD");
        System.out.println(rater.getRate());
    }
}
	