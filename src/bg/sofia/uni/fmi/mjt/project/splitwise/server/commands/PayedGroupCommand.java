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
        final int NUMBER_OF_ARGUMENTS = 4;
        if (tokens.length == NUMBER_OF_ARGUMENTS) {
            payed(tokens);
        } else {
            writer.println(ERROR_MESSAGE);
        }
    }

    private void payed(String[] tokens) {
        final int INDEX_OF_AMOUNT = 1;
        double amount = Double.parseDouble(tokens[INDEX_OF_AMOUNT]);

        final int INDEX_OF_GROUP = 2;
        String group = tokens[INDEX_OF_GROUP];

        final int INDEX_OF_FRIEND = 3;
        String friend = tokens[INDEX_OF_FRIEND];

        server.decreaseAmountOfGroupMember(username, group, friend, amount);
        server.increaseAmountOfGroupMember(friend, group, username, amount);
        Domain domain = getDomain();
        Messenger messenger = new Messenger(domain);
        messenger.sendGroupMessageAfterPayed(amount, friend);
    }
}
