package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Commands;

import java.io.PrintWriter;

public class SplitMoneyCommand extends Command {

    private Server server;
    private PrintWriter writer;
    private String username;

    public SplitMoneyCommand(Domain domain) {
        super(domain);
        server = getDomain().getServer();
        writer = getDomain().getWriter();
        username = getDomain().getUsername();
    }


    @Override
    public void executeCommand(String[] tokens) {
        String command = tokens[INDEX_OF_COMMAND];
        if (isMatched(command)) {
            splitMoney(tokens);
        }
    }

    @Override
    protected boolean isMatched(String command) {
        return Commands.SPLIT.getCommand().equals(command);
    }

    private void splitMoney(String[] tokens) {
        if (tokens.length != 4) {
            writer.println(ERROR_MESSAGE);
        } else {
            String friend = tokens[2];

            checkIsUsernameContained(writer, friend);
            checkIsUserInFriendList(friend, writer);

            String amount = tokens[1];
            transactMoney(friend, amount);

            String reasonForPayment = tokens[3];

            sendPaymentMessage(amount, friend, reasonForPayment);
            printCurrentStatus(friend);
            sendFriendNotification(amount, reasonForPayment, friend);
        }
    }

    private void sendFriendNotification(String amount, String reasonForPayment, String friend) {
        String friendMessage = String.format("You owe  %s %s %s %n", server.getProfileNames(username), amount,
                reasonForPayment);
        sendFriendNotification(friend, friendMessage);
    }

    private void checkIsUsernameContained(PrintWriter writer, String friend) {
        if (isUsernameNotContained(friend)) {
            writer.println(String.format("User with name %s does not exists.", friend));
            return;
        }
    }

    private boolean isUsernameNotContained(String friend) {
        return !(server.isUsernameContained(friend));
    }

    private void checkIsUserInFriendList(String friend, PrintWriter writer) {
        if (isUserNotInFriendList(friend)) {
            writer.println(String.format(
                    "This user %s is not in your friend list, you have to added before splitting money.", friend));
            return;
        }
    }

    private boolean isUserNotInFriendList(String friend) {
        return !(server.isUserInFriends(username, friend));
    }

    private void transactMoney(String friend, String preAmount) {
        double amount = Double.parseDouble(preAmount) / 2;
        server.increaseAmountOfFriend(username, friend, amount);
        server.decreaseAmountOfFriend(friend, username, amount);
    }

    private void sendPaymentMessage(String amount, String friend, String reasonForPayment) {
        String paymentMessage = String.format("Split %s  between you and %s for %s.%n", amount,
                friend, reasonForPayment);
        writer.printf(paymentMessage);
        writeInPaymentFile(paymentMessage);
    }

    private void printCurrentStatus(String friend) {
        StringBuilder message = new StringBuilder();
        message.append("Current status: ");
        String username = getDomain().getUsername();
        getStatusForOneClient(message, server.getFriendAmount(username, friend));
        writer.println(message.toString());
    }

    private void writeInPaymentFile(String paymentMessage) {
        String username = getDomain().getUsername();
        server.writeInPaymentFile(paymentMessage, username);
    }

    private void getStatusForOneClient(StringBuilder message, double amount) {
        double result = amountAfterRoundUp(amount);
        if (amount > 0) {
            message.append(String.format("Owes you %s %s.", result));
        } else if (amount < 0) {
            final int MINUS = -1;
            message.append(String.format("You owe %s %s", MINUS * result));
        } else {
            message.append("Good accounts good friends");
        }
    }

    private double amountAfterRoundUp(double amount) {
        double scale = Math.pow(10, 2);
        return Math.round(amount * scale) / scale;
    }

    private void sendFriendNotification(String friend, String friendMessage) {
        server.sendFriendNotification(friend, friendMessage);
    }
}
