package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;

import java.io.PrintWriter;

public class CommandSplitMoney extends ActionCommand {

    private Server server;

    public CommandSplitMoney(Domain domain, PrintWriter writer) {
        super(domain, writer);
        setServer();
    }

    private void setServer() {
        server = getDomain().getServer();
    }

    @Override
    public void executeCommand(String[] tokens) {
        splitMoney(tokens);
    }

    @Override
    protected boolean isMatched(String command) {
        return false;
    }

    private void splitMoney(String[] tokens) {
        PrintWriter writer = getWriter();
        if (tokens.length != 4) {
            writer.println(ERROR_MESSAGE);
        } else {

            String friend = tokens[2];
            String username = getDomain().getUsername();

            checkIsUsernameContained(writer, friend);
            checkIsUserInFriendList(username, friend, writer);

            String amount = tokens[1];
            transactMoney(username, friend, amount);

            //first method
            String paymentMessage = String.format("Split %s  between you and %s for %s.%n", amount,
                    friend, tokens[3]);
            writer.printf(paymentMessage);
            writeInPaymentFile(paymentMessage);

            //second method
            StringBuilder message = new StringBuilder();
            message.append("Current status: ");
            getStatusForOneClient(message, server.getFriendAmount(username, friend));
            writer.println(message.toString());

            //third method
            String reasonForPayment = tokens[3];
            String friendMessage = String.format("You owe  %s %s %s %n", server.getProfileNames(username), amount,
                    reasonForPayment);
            sendFriendNotification(friend, friendMessage);
        }
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

    private void checkIsUserInFriendList(String username, String friend, PrintWriter writer) {
        if (isUserNotInFriendList(username, friend)) {
            writer.println(String.format(
                    "This user %s is not in your friend list, you have to added before splitting money.", friend));
            return;
        }
    }

    private boolean isUserNotInFriendList(String username, String friend) {
        return !(server.isUserInFriends(username, friend));
    }

    /////////////////////
    private void transactMoney(String username, String friend, String s) {
        double amount = Double.parseDouble(s) / 2;
        server.increaseAmountOfFriend(username, friend, amount);
        server.decreaseAmountOfFriend(friend, username, amount);
    }

    private void writeInPaymentFile(String paymentMessage) {

    }

    private void getStatusForOneClient(StringBuilder message, double amonut) {

    }

    private void sendFriendNotification(String friend, String friendMessage) {

    }
}
