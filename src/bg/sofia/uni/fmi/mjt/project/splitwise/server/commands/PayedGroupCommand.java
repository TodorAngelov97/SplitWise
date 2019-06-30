package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.commands.utilities.Messenger;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Commands;

import java.io.PrintWriter;

public class PayedGroupCommand extends ActionCommand {
    private Server server;
    private PrintWriter writer;
    private String username;

    public PayedGroupCommand(Domain domain) {
        super(domain);
        server = getDomain().getServer();
        username = getDomain().getUsername();
        writer = getDomain().getWriter();
    }


    @Override
    public void executeCommand(String[] tokens) {
        String command = tokens[INDEX_OF_COMMAND];
        if (isMatched(command)) {
            payedGroup(tokens);
        }
    }

    @Override
    protected boolean isMatched(String command) {
        return Commands.PAYED_GROUP.getCommand().equals(command);
    }

    private void payedGroup(String[] tokens) {
        if (tokens.length != 4) {
            writer.println(ERROR_MESSAGE);
        } else {
            double amount = Double.parseDouble(tokens[1]);
            String group = tokens[2];
            String friend = tokens[3];
            server.decreaseAmountOfGroupMember(username, group, friend, amount);
            server.increaseAmountOfGroupMember(friend, group, username, amount);
            Domain domain = getDomain();
            Messenger messenger = new Messenger(domain, writer);
            messenger.sendGroupMessageAfterPayed(amount, friend);
        }
    }
}
