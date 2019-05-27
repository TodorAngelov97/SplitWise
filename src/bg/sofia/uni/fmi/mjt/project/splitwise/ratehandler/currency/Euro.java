package bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler.currency;

public class Euro implements Currency {

	private double EUR;

	public Euro(double EUR) {
		this.EUR = EUR;
	}

	@Override
	public double getRate() {
		
		return EUR;
	}

}
