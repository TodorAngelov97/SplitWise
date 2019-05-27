package bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler;

import bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler.currency.Leva;

public class LevaRate {
	private Leva rates;

	public LevaRate(Leva rates) {
		this.rates = rates;
	}

	public double getRate() {
		return rates.getRate();
	}
}
