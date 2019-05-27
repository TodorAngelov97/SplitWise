package bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler.currency;

public class Dollar implements Currency {

	private double USD;

	public Dollar(double USD) {
		this.USD = USD;
	}

	@Override
	public double getRate() {
		return USD;
	}

}
