package bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler.currency;

public class Lev implements Currency {

	private double BGN;

	public Lev(double BGN) {
		this.BGN = BGN;
	}

	@Override
	public double getRate() {
		return BGN;
	}

}
