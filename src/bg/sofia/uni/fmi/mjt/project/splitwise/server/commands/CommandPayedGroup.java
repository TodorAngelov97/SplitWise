package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.commands.utilities.Messenger;

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
    public void executeCommand(String[] tokens) throws IOException {
        String command = tokens[INDEX_OF_COMMAND];
        if (isMatched(command)) {
            payedGroup(tokens);
        }
    }

    @Override
    protected boolean isMatched(String command) {
        if ("payed-group".equals(command)) {
            return true;
        }
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
            server.decreaseAmountOfGroupMember(username, group, friend, amount);
            server.increaseAmountOfGroupMember(friend, group, username, amount);
            Domain domain = getDomain();
            Messenger messenger = new Messenger(domain, writer);
            messenger.sendGroupMessageAfterPayed(amount, friend);
        }
    }
}
