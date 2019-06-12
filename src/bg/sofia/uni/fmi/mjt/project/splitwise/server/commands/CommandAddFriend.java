package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;

import java.io.PrintWriter;

public class CommandAddFriend extends ActionCommand {


    public CommandAddFriend(Domain domain, PrintWriter writer) {
        super(domain, writer);
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
        if ("add-friend".equals(command)) {
            return true;
        }
        return false;
    }

    private void addFriend(String[] tokens) {

        if (tokens.length == 2) {
            add(tokens);
        } else {
            PrintWriter writer = getWriter();
            writer.println(ERROR_MESSAGE);
        }
    }

    private void add(String[] tokens) {

        Server server = getDomain().getServer();
        final int FRIEND_INDEX = 1;
        String friend = tokens[FRIEND_INDEX];
        PrintWriter writer = getWriter();
        String username = getDomain().getUsername();
        if (!server.isUsernameContained(friend)) {
            writer.println(String.format("User with name: %s does not exists.", friend));
        } else if (server.isUserInFriends(username, friend)) {
            writer.println(String.format("User with name: %s already in your friend list.", friend));
        } else if (username.equals(friend)) {
            writer.println("You can not add yourself as a friend.");
        } else {
            addToFriendList(username, friend);
            String message = String.format("%s added you as friend. %n", server.getProfileNames(username));
            sendFriendNotification(friend, message);
        }
    }

    private void addToFriendList(String username, String friend) {
        Server server = getDomain().getServer();
        server.addFriend(username, friend);
        server.addFriend(friend, username);

        PrintWriter writer = getWriter();
        writer.println("Successfully added friend with name: " + friend);
    }

    //NOT YET FIGURE OUT THE LOGIC
    private void sendFriendNotification(String friend, String message) {
        Server server = getDomain().getServer();
        server.sendFriendNotification(friend, message);
    }
}
