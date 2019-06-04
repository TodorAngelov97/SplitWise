package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;

import java.io.IOException;
import java.io.PrintWriter;

public class CommandPayedGroup extends ActionCommand {


    private Server server;

    public CommandPayedGroup(Domain domain, PrintWriter writer) {
        super(domain, writer);
        setServer();
    }

    private void setServer() {
        server = getDomain().getServer();
    }

    @Override
    public void executeCommand(String[] tokens) {
//        payedGroup(tokens);
    }

    @Override
    protected boolean isMatched(String command) {
        return false;
    }

    private void payedGroup(String[] tokens) throws IOException {
        PrintWriter writer = getWriter();
        if (tokens.length != 4) {
            writer.println(ERROR_MESSAGE);
        } else {
            double amount = Double.parseDouble(tokens[1]);
            String group = tokens[2];
            String friend = tokens[3];
            String username = getDomain().getUsername();
            server.getGroupsOfUser(username).get(group).decreaseAmountOfFriend(friend, amount);
            server.getGroupsOfUser(friend).get(group).increaseAmountOfFriend(username, amount);
            sendMessageAfterPayed(writer, amount, friend, true);
        }
    }

    private void sendMessageAfterPayed(PrintWriter writer, double amount, String friend, boolean b) {

    }


}
