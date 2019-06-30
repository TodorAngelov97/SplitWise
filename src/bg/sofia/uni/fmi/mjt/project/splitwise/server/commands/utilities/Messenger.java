package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands.utilities;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;

import java.io.PrintWriter;

public class Messenger {

    private Domain domain;
    private PrintWriter writer;

    public Messenger(Domain domain, PrintWriter writer) {
        this.domain = domain;
        this.writer = writer;
    }

    public void sendFriendMessageAfterPayed(double amount, String friend) {
        Server server = domain.getServer();
        String messageForFriend = sendMessageAfterPayed(amount, friend);
        server.sendFriendNotification(friend, messageForFriend);
    }

    public void sendGroupMessageAfterPayed(double amount, String friend) {
        Server server = domain.getServer();
        String messageForFriend = sendMessageAfterPayed(amount, friend);
        server.sendGroupNotification(friend, messageForFriend);
    }

    private String sendMessageAfterPayed(double amount, String friend) {

        StringBuilder message = new StringBuilder();
        message.append("Current status: ");
        StatusForFriend.getStatusForOneFriend(message, amount);
        message.append(String.format("%nYou payed %s to %s", amount, friend));

        writer.println(message.toString());
        Server server = domain.getServer();
        String username = domain.getUsername();
        String messageForFriend = String.format("%s approved your payment %s %s", server.getProfileNames(username),
                amount);
        return messageForFriend;
    }
}
