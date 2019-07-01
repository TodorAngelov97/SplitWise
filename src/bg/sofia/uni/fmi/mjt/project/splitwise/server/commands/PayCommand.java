package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.commands.utilities.Messenger;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Commands;

import java.io.PrintWriter;

public class PayCommand extends ActionCommand {

    private Server server;
    private PrintWriter writer;
    private String username;

    public PayCommand(Domain domain) {
        super(domain);
        server = getDomain().getServer();
        writer = getDomain().getWriter();
        username = getDomain().getUsername();
    }

    @Override
    public void executeCommand(String[] tokens) {
        String command = tokens[INDEX_OF_COMMAND];
        if (isMatched(command)) {
            getPayed(tokens);
        }
    }

    @Override
    protected boolean isMatched(String command) {
        return Commands.PAYED.getCommand().equals(command);
    }

    private void getPayed(String[] tokens) {
        final int NUMBER_OF_ARGUMENTS = 3;
        if (tokens.length == NUMBER_OF_ARGUMENTS) {
            payed(tokens);
        } else {
            writer.println(ERROR_MESSAGE);
        }
    }

    private void payed(String[] tokens) {
        final int INDEX_OF_FRIEND = 2;
        String friend = tokens[INDEX_OF_FRIEND];
        final int INDEX_OF_AMOUNT = 1;
        double amount = Double.parseDouble(tokens[INDEX_OF_AMOUNT]);

        server.decreaseAmountOfFriend(username, friend, amount);
        server.increaseAmountOfFriend(friend, username, amount);
        Domain domain = getDomain();
        Messenger messenger = new Messenger(domain);
        messenger.sendFriendMessageAfterPayed(amount, friend);
    }
}
