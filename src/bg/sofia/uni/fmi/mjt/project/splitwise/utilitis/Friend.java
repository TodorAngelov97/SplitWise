package bg.sofia.uni.fmi.mjt.project.splitwise.utilitis;

public class Friend {
    private double amount;

    public Friend() {
        amount = 0;
    }

    public double getAmount() {
        return Math.round(amount * 100.00) / 100.00;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void increase(double amount) {
        this.amount += amount;
    }

    public void decrease(double amount) {
        this.amount -= amount;
    }

}
