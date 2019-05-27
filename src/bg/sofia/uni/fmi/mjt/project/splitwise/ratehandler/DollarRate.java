package bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler;

import bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler.currency.Dollar;

public class DollarRate {
	private Dollar rates;

	public DollarRate(Dollar rates) {
		this.rates = rates;
	}

	public double getRate() {
		return rates.getRate();
	}
}
