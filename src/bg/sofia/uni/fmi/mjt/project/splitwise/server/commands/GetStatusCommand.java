package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.commands.utilities.StatusForFriend;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Commands;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Friend;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Group;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

public class GetStatusCommand extends ActionCommand {

    private Server server;
    private PrintWriter writer;
    private String username;

    public GetStatusCommand(Domain domain) {
        super(domain);
        server = getDomain().getServer();
        writer = getDomain().getWriter();
        username = getDomain().getUsername();
    }


    @Override
    public void executeCommand(String[] tokens) {
        String command = tokens[INDEX_OF_COMMAND];
        if (isMatched(command)) {
            getStatus();
        }
    }

    @Override
    protected boolean isMatched(String command) {
        return Commands.GET_STATUS.getCommand().equals(command);
    }

    private void getStatus() {

        if (areListsEmpty(username)) {
            writer.println("You don't have any added friends and groups");
            return;
        }
        if (!isFriendListEmpty(username)) {
            writer.println("Friends:");
            getStatusForFriends(server.getFriends(username).entrySet(), writer);
        }
        if (!isGroupListEmpty(username)) {
            writer.println("Groups:");
            getStatusForGroups(server.getGroups(username).entrySet(), writer);
        }
    }

    private boolean areListsEmpty(String username) {
        return isFriendListEmpty(username) && isGroupListEmpty(username);
    }

    private boolean isFriendListEmpty(String username) {
        return server.hasNotFriends(username);
    }

    private boolean isGroupListEmpty(String username) {
        return server.hasNotGroups(username);
    }

    private void getStatusForFriends(Set<Map.Entry<String, Friend>> allFriends, PrintWriter writer) {

        for (Map.Entry<String, Friend> friend : allFriends) {
            StringBuilder message = new StringBuilder();
            message.append(String.format("* %s (%s): ", server.getProfileNames(friend.getKey()), friend.getKey()));
            StatusForFriend.getStatusForOneFriend(message, friend.getValue().getAmount());
            writer.println(message.toString());
        }
    }

    private void getStatusForGroups(Set<Map.Entry<String, Group>> allGroups, PrintWriter writer) {

        for (Map.Entry<String, Group> group : allGroups) {
            writer.println(String.format("* %s", group.getKey()));
            getStatusForFriends(server.getMembersInGroup(username, group.getKey()), writer);
        }
    }
}
