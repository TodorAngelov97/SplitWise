package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Commands;

import java.io.PrintWriter;

public class AddFriendCommand extends ActionCommand {

    private Server server;
    private String username;
    private PrintWriter writer;

    public AddFriendCommand(Domain domain) {
        super(domain);
        server = getDomain().getServer();
        writer = getDomain().getWriter();
        username = getDomain().getUsername();
    }

    @Override
    public void executeCommand(String[] tokens) {

        String command = tokens[INDEX_OF_COMMAND];
        if (isMatched(command)) {
            addFriend(tokens);
        }
    }

    @Override
    protected boolean isMatched(String command) {
        return Commands.ADD.getCommand().equals(command);
    }

    private void addFriend(String[] tokens) {

        if (tokens.length == 2) {
            add(tokens);
        } else {
            writer.println(ERROR_MESSAGE);
        }
    }

    private void add(String[] tokens) {

        final int FRIEND_INDEX = 1;
        String friend = tokens[FRIEND_INDEX];
        if (!server.isUsernameContained(friend)) {
            writer.println(String.format("User with name: %s does not exists.", friend));
        } else if (server.isUserInFriends(username, friend)) {
            writer.println(String.format("User with name: %s already in your friend list.", friend));
        } else if (username.equals(friend)) {
            writer.println("You can not add yourself as a friend.");
        } else {
            addToFriendList(friend);
            String message = String.format("%s added you as friend. %n", server.getProfileNames(username));
            sendFriendNotification(friend, message);
        }
    }

    private void addToFriendList(String friend) {
        server.addFriend(username, friend);
        server.addFriend(friend, username);
        writer.println("Successfully added friend with name: " + friend);
    }

    private void sendFriendNotification(String friend, String message) {
        server.sendFriendNotification(friend, message);
    }
}
