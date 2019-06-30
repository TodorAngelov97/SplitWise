package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands.utilities;

public class StatusForFriend {

    public static void getStatusForOneFriend(StringBuilder messageLine, double amount) {

        double result = amountAfterRoundUp(amount);
        if (amount > 0) {
            messageLine.append(String.format("Owes you %s %s.", result));
        } else if (amount < 0) {
            final int MINUS = -1;
            messageLine.append(String.format("You owe %s %s", MINUS * result));
        } else {
            messageLine.append("Good accounts good friends");
        }
    }

    private static double amountAfterRoundUp(double amount) {
        double scale = Math.pow(10, 2);
        return Math.round(amount * scale) / scale;
    }

}
