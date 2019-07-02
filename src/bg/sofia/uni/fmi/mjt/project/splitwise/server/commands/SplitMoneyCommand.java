package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.commands.utilities.StatusForFriend;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Commands;

import java.io.PrintWriter;

public class SplitMoneyCommand extends ActionCommand {

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
        final int NUMBER_OF_ARGUMENTS = 4;
        if (tokens.length == NUMBER_OF_ARGUMENTS) {
            split(tokens);
        } else {
            writer.println(ERROR_MESSAGE);
        }
    }

    private void split(String[] tokens) {
        final int INDEX_OF_FRIEND = 2;
        String friend = tokens[INDEX_OF_FRIEND];

        checkIsUsernameContained(writer, friend);
        checkIsUserInFriendList(friend, writer);

        final int INDEX_OF_AMOUNT = 1;
        String amount = tokens[INDEX_OF_AMOUNT];
        transactMoney(friend, amount);

        final int INDEX_OF_PAYMENT_REASON = 3;
        String reasonForPayment = tokens[INDEX_OF_PAYMENT_REASON];

        sendPaymentMessage(amount, friend, reasonForPayment);
        printCurrentStatus(friend);
        sendFriendNotification(amount, reasonForPayment, friend);
    }

    private void sendFriendNotification(String amount, String reasonForPayment, String friend) {
        String friendMessage = String.format("You owe  %s %s %s %n", server.getProfileNames(username), amount,
                reasonForPayment);
        sendFriendNotification(friend, friendMessage);
    }

    private void checkIsUsernameContained(PrintWriter writer, String friend) {
        if (isUsernameNotContained(friend)) {
            String message = String.format("User with name %s does not exists.", friend);
            writer.println(message);
            return;
        }
    }

    private boolean isUsernameNotContained(String friend) {
        return !(server.isUsernameContained(friend));
    }

    private void checkIsUserInFriendList(String friend, PrintWriter writer) {
        if (isUserNotInFriendList(friend)) {
            String message = String.format(
                    "This user %s is not in your friend list, you have to added before splitting money.", friend);
            writer.println(message);
            return;
        }
    }

    private boolean isUserNotInFriendList(String friend) {
        return !(server.isUserInFriends(username, friend));
    }

    private void transactMoney(String friend, String preAmount) {
        final int DIVIDER = 2;
        double amount = Double.parseDouble(preAmount) / DIVIDER;
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
        StatusForFriend.getStatusForOneFriend(message, server.getFriendAmount(username, friend));
        writer.println(message.toString());
    }

    private void writeInPaymentFile(String paymentMessage) {
        String username = getDomain().getUsername();
        server.writeInPaymentFile(paymentMessage, username);
    }

    private void sendFriendNotification(String friend, String friendMessage) {
        server.sendFriendNotification(friend, friendMessage);
    }
}
