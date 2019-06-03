package bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler;

import bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler.currency.Lev;

public class LevRate {
	private Lev rates;

	public LevRate(Lev rates) {
		this.rates = rates;
	}

	public double getRate() {
		return rates.getRate();
	}
}