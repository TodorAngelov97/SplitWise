package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands.utilities;

public class StatusForFriend {

    public static void getStatusForOneFriend(StringBuilder messageLine, double amount) {


        double result = amountAfterRoundUp(amount);

        final int DELIMITATION = 0;
        if (amount > DELIMITATION) {
            String message = String.format("Owes you %s %s.", result);
            messageLine.append(message);
        } else if (amount < DELIMITATION) {
            final int MINUS = -1;
            String message = String.format("You owe %s %s", MINUS * result);
            messageLine.append(message);
        } else {
            final String MESSAGE = "Good accounts good friends";
            messageLine.append(MESSAGE);
        }
    }

    private static double amountAfterRoundUp(double amount) {
        double scale = Math.pow(10, 2);
        return Math.round(amount * scale) / scale;
    }
}
