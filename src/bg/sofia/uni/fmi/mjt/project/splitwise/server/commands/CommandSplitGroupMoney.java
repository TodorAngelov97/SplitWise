package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;

import java.io.IOException;
import java.io.PrintWriter;

public class CommandSplitGroupMoney extends ActionCommand {

    public CommandSplitGroupMoney(Domain domain, PrintWriter writer) {
        super(domain, writer);
    }

    @Override
    public void executeCommand(String[] tokens) throws IOException {
        String command = tokens[INDEX_OF_COMMAND];
        if (isMatched(command)) {
            splitGroupMoney(tokens);
        }
    }

    @Override
    protected boolean isMatched(String command) {
        if ("split-group".equals(command)) {
            return true;
        }
        return false;
    }

    private void splitGroupMoney(String[] tokens) {
        if (tokens.length == 4) {
            String group = tokens[2];
            String amount = tokens[1];
            transactMoney(amount, group);

            String reasonForPayment = tokens[3];
            sendPaymentMessage(amount, group, reasonForPayment);

            sendGroupNotification(group, amount, reasonForPayment);
        }
    }

    private void transactMoney(String amountInString, String group) {
        String username = getDomain().getUsername();
        Server server = getDomain().getServer();
        double amount = getAmountPerFriend(amountInString, group);
        for (String friend : server.getMembersNamesInGroup(username, group)) {
            server.decreaseAmountOfGroupMember(friend, group, username, amount);
            server.increaseAmountOfGroupMember(username, group, friend, amount);
        }
    }

    private double getAmountPerFriend(String amountInString, String group) {
        double initialSum = Double.parseDouble(amountInString);
        String username = getDomain().getUsername();
        Server server = getDomain().getServer();
        int membersCount = server.getNumberOfMembersInGroup(username, group);
        double amount = initialSum / membersCount;
        return amount;
    }

    private void sendPaymentMessage(String amount, String friend, String reasonForPayment) {
        String paymentMessage = String.format("Split %s  between you and %s for %s.%n", amount,
                friend, reasonForPayment);
        PrintWriter writer = getWriter();
        writer.printf(paymentMessage);
        writeInPaymentFile(paymentMessage);
    }

    private void writeInPaymentFile(String paymentMessage) {
        Server server = getDomain().getServer();
        String username = getDomain().getUsername();
        server.writeInPaymentFile(paymentMessage, username);
    }

    private void sendGroupNotification(String group, String amountInString, String reasonForPayment) {
        Server server = getDomain().getServer();
        String username = getDomain().getUsername();
        double amount = getAmountPerFriend(amountInString, group);
        for (String memberOfTheGroup : server.getMembersNamesInGroup(username, group)) {
            String message = String.format("* %s:%nYou owe %s  %s %s", group, server.getProfileNames(username),
                    amount, reasonForPayment);
            server.sendGroupNotification(memberOfTheGroup, message);
        }
    }
}
