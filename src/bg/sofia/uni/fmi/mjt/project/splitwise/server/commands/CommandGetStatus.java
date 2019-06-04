package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.Friend;
import bg.sofia.uni.fmi.mjt.project.splitwise.Group;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

public class CommandGetStatus extends ActionCommand {

    private Server server;

    public CommandGetStatus(Domain domain, PrintWriter writer) {
        super(domain, writer);
        setServer();
    }

    private void setServer() {
        server = getDomain().getServer();
    }

    @Override
    public void executeCommand(String[] tokens) {
        getStatus();
    }

    @Override
    protected boolean isMatched(String command) {
        return false;
    }


    //refactoring big big
    private void getStatus() {

        String username = getDomain().getUsername();
        PrintWriter writer = getWriter();
        //checkIsListEmpty ?
        if (areListsEmpty(username)) {
            writer.println("You don't have any added friends and groups");
            return;
        }
        //one method
        if (!isFriendListEmpty(username)) {
            writer.println("Friends:");
            getStatusForFriends(server.getFriendsList(username).entrySet(), writer);
        }
        if (!isGroupListEmpty(username)) {
            writer.println("Groups:");
            getStatusForGroups(server.getGroupsOfUser(username).entrySet(), writer);
        }
    }

    private boolean areListsEmpty(String username) {
        return isFriendListEmpty(username) && isGroupListEmpty(username);
    }

    private boolean isFriendListEmpty(String username) {
        return server.getFriendsList(username).isEmpty();
    }

    private boolean isGroupListEmpty(String username) {
        return server.getGroupsOfUser(username).isEmpty();
    }


    private void getStatusForFriends(Set<Map.Entry<String, Friend>> allFriends, PrintWriter writer) {

        for (Map.Entry<String, Friend> friend : allFriends) {
            StringBuilder message = new StringBuilder();
            message.append(String.format("* %s (%s): ", server.getProfileNames(friend.getKey()), friend.getKey()));
            getStatusForOneClient(message, friend.getValue().getAmount());
            writer.println(message.toString());
        }
    }

    private void getStatusForOneClient(StringBuilder messageLine, double amount) {

        double result = amountAfterRoundUp(amount);
        if (amount > 0) {
            messageLine.append(String.format("Owes you %s %s.", result));
        } else if (amount < 0) {
            final int MINUS = -1;
            messageLine.append(String.format("You owe %s %s", MINUS * result));
        } else {
            messageLine.append("Good accounts good friends");
        }
    }

    private double amountAfterRoundUp(double amount) {
        double scale = Math.pow(10, 2);
        return Math.round(amount * scale) / scale;
    }

    private void getStatusForGroups(Set<Map.Entry<String, Group>> allGroups, PrintWriter writer) {

        String username = getDomain().getUsername();
        for (Map.Entry<String, Group> group : allGroups) {
            writer.println(String.format("* %s", group.getKey()));
            getStatusForFriends(server.getGroupsOfUser(username).get(group.getKey()).getAllMembersInGroup(), writer);
        }
    }
}
