package bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler;

import bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler.rate.DollarRate;
import bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler.rate.EuroRate;
import bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler.rate.LevRate;
import bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler.rate.Rate;
import bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler.utilities.Currencies;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

public class RateHandler {
    private static final String ERROR_MESSAGE = "Problem with currency switch, try again later";
    private String currentCurrency;
    private String wantedCurrency;

    public RateHandler(String fromCurrency, String toCurrency) {
        this.currentCurrency = fromCurrency;
        this.wantedCurrency = toCurrency;
    }

    private String getJson() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String req = String
                .format("https://api.exchangeratesapi.io/latest?base=%s&symbols=%s", currentCurrency,
                        wantedCurrency);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(req))
                .build();
        return client.send(request, BodyHandlers.ofString()).body();
    }

    public double getRate() {
        Gson gson = new Gson();
        Rate rate = null;
        try {
            if (wantedCurrency.equals(Currencies.EUR.getCurrency())) {
                rate = gson.fromJson(getJson(), EuroRate.class);
            } else if (wantedCurrency.equals(Currencies.USD.getCurrency())) {
                rate = gson.fromJson(getJson(), DollarRate.class);
            }
            rate = gson.fromJson(getJson(), LevRate.class);
        } catch (JsonSyntaxException e) {
            System.err.println("Exception thrown by Json: " + e.getMessage());
            System.out.println(ERROR_MESSAGE);
        } catch (IOException e) {
            System.err.println("Exception thrown by Json: " + e.getMessage());
            System.out.println(ERROR_MESSAGE);
        } catch (InterruptedException e) {
            System.err.println("Exception thrown by Json: " + e.getMessage());
            System.out.println(ERROR_MESSAGE);
        }
        return rate.getRate();
    }

    public static void main(String[] args) {
//        HttpClient client = HttpClient.newHttpClient();
//        String req = String.format("https://api.exchangeratesapi.io/latest?base=%s&symbols=%s", "EUR", "USD");
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(req))
//                .build();
//        try {
//            Gson gson = new Gson();
//            DollarRate rate = gson.fromJson(client.send(request, BodyHandlers.ofString()).body(), DollarRate.class);
//            System.out.println(rate.getRate());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        RateHandler rater = new RateHandler("EUR", "USD");
//        System.out.println(rater.getRate());
    }
}
	