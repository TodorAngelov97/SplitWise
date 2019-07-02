package bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler.rate;

import bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler.currency.Lev;

public class LevRate implements Rate {
    private Lev rates;

    public LevRate(Lev rates) {
        this.rates = rates;
    }

    @Override
    public double getRate() {
        return rates.getRate();
    }
}
