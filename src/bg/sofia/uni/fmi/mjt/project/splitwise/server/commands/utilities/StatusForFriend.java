package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands.utilities;

public class StatusForFriend {

    public static void getStatusForOneFriend(StringBuilder messageLine, double amount) {

        double result = amountAfterRoundUp(amount);
        if (amount > 0) {
            String message = String.format("Owes you %s %s.", result);
            messageLine.append(message);
        } else if (amount < 0) {
            final int MINUS = -1;
            String message = String.format("You owe %s %s", MINUS * result);
            messageLine.append(message);
        } else {
            String message = "Good accounts good friends";
            messageLine.append(message);
        }
    }

    private static double amountAfterRoundUp(double amount) {
        double scale = Math.pow(10, 2);
        return Math.round(amount * scale) / scale;
    }
}
