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
        double amount = getAmountPerFriend(amountInString, group);
//        add better signature
        server.getGroups(username).get(group).splitMoney(amount);
        for (String friend : server.getMembersNamesInGroup(username, group)) {
            server.decreaseAmountOfGroupMember(friend, group, username, amount);
//            server.increaseAmountOfGroupMember(username, group, friend, amount);
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
