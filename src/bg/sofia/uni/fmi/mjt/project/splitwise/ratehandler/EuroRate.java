package bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler;

import bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler.currency.Euro;

public class EuroRate implements Rate {
    private Euro rates;

    public EuroRate(Euro rates) {
        this.rates = rates;
    }

    @Override
    public double getRate() {
        return rates.getRate();
    }
}
