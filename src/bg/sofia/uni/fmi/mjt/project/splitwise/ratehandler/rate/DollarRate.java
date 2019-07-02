package bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler.rate;

import bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler.currency.Dollar;

public class DollarRate implements Rate {
	private Dollar rates;

	public DollarRate(Dollar rates) {
		this.rates = rates;
	}

	@Override
	public double getRate() {
		return rates.getRate();
	}
}
