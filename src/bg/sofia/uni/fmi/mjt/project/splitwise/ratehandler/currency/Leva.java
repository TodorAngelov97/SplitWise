package bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler.currency;

public class Leva implements Currency {

	private double BGN;

	public Leva(double BGN) {
		this.BGN = BGN;
	}

	@Override
	public double getRate() {
		return BGN;
	}

}
