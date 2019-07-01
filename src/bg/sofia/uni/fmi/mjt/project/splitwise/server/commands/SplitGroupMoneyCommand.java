package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Commands;

import java.io.PrintWriter;

public class SplitGroupMoneyCommand extends ActionCommand {


    private Server server;
    private String username;
    private PrintWriter writer;

    public SplitGroupMoneyCommand(Domain domain) {
        super(domain);
        server = getDomain().getServer();
        username = getDomain().getUsername();
        writer = getDomain().getWriter();
    }

    @Override
    public void executeCommand(String[] tokens) {
        String command = tokens[INDEX_OF_COMMAND];
        if (isMatched(command)) {
            splitGroupMoney(tokens);
        }
    }

    @Override
    protected boolean isMatched(String command) {
        return Commands.SPLIT_GROUP.getCommand().equals(command);
    }

    private void splitGroupMoney(String[] tokens) {
        final int NUMBER_OF_ARGUMENTS = 4;

        if (tokens.length == NUMBER_OF_ARGUMENTS) {
            final int INDEX_OF_GROUP = 2;
            String group = tokens[INDEX_OF_GROUP];

            final int INDEX_OF_AMOUNT = 1;
            String amount = tokens[INDEX_OF_AMOUNT];
            transactMoney(amount, group);

            final int INDEX_OF_PAYMENT_REASON = 3;
            String reasonForPayment = tokens[INDEX_OF_PAYMENT_REASON];
            sendPaymentMessage(amount, group, reasonForPayment);
            sendGroupNotification(group, amount, reasonForPayment);
        }
    }

    private void transactMoney(String amountInString, String group) {
        double amount = getAmountPerFriend(amountInString, group);
        server.increaseAmountOfGroup(username, group, amount);
        for (String friend : server.getMembersNamesInGroup(username, group)) {
            server.decreaseAmountOfGroupMember(friend, group, username, amount);
        }
    }

    private double getAmountPerFriend(String amountInString, String group) {
        double initialSum = Double.parseDouble(amountInString);
        int membersCount = server.getNumberOfMembersInGroup(username, group);
        double amount = initialSum / membersCount;
        return amount;
    }

    private void sendPaymentMessage(String amount, String friend, String reasonForPayment) {
        String paymentMessage = String.format("Split %s  between you and %s for %s.%n", amount,
                friend, reasonForPayment);
        writer.printf(paymentMessage);
        writeInPaymentFile(paymentMessage);
    }

    private void writeInPaymentFile(String paymentMessage) {
        server.writeInPaymentFile(paymentMessage, username);
    }

    private void sendGroupNotification(String group, String amountInString, String reasonForPayment) {
        double amount = getAmountPerFriend(amountInString, group);
        for (String memberOfTheGroup : server.getMembersNamesInGroup(username, group)) {
            String message = String.format("* %s:%nYou owe %s  %s %s", group, server.getProfileNames(username),
                    amount, reasonForPayment);
            server.sendGroupNotification(memberOfTheGroup, message);
        }
    }
}
