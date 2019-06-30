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
        if (tokens.length != 3) {
            writer.println(ERROR_MESSAGE);
        } else {
            String friend = tokens[2];
            double amount = Double.parseDouble(tokens[1]);
            server.decreaseAmountOfFriend(username, friend, amount);
            server.increaseAmountOfFriend(friend, username, amount);
            Domain domain = getDomain();
            Messenger messenger = new Messenger(domain);
            messenger.sendFriendMessageAfterPayed(amount, friend);
        }
    }
}
