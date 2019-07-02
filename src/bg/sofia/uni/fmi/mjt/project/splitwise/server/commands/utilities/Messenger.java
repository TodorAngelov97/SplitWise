package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands.utilities;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;

import java.io.PrintWriter;

public class Messenger {

    private Server server;
    private String username;
    private PrintWriter writer;

    public Messenger(Domain domain) {
        this.server = domain.getServer();
        this.username = domain.getUsername();
        this.writer = domain.getWriter();
    }

    public void sendFriendMessageAfterPayed(double amount, String friend) {
        printMessage(amount, friend);
        String messageForFriend = getMessageForFriendAfterPayed(amount);
        server.sendFriendNotification(friend, messageForFriend);
    }

    private void printMessage(double amount, String friend) {
        StringBuilder message = new StringBuilder();
        final String LEAD_LINE = "Current status: ";
        message.append(LEAD_LINE);
        StatusForFriend.getStatusForOneFriend(message, amount);
        String payedLine = String.format("%nYou payed %s to %s", amount, friend);
        message.append(payedLine);
        writer.println(message.toString());
    }

    private String getMessageForFriendAfterPayed(double amount) {
        String messageForFriend = String.format("%s approved your payment %s %s", server.getProfileNames(username),
                amount);
        return messageForFriend;
    }

    public void sendGroupMessageAfterPayed(double amount, String friend) {
        printMessage(amount, friend);
        String messageForFriend = getMessageForFriendAfterPayed(amount);
        server.sendGroupNotification(friend, messageForFriend);
    }
}
