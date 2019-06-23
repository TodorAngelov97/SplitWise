package bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler;

public enum Currencies {

    BGN("BGN"),
    USD("USD"),
    EUR("EUR");

    private final String currency;

    Currencies(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return this.currency;
    }
}
