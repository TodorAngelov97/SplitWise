package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;

import java.io.PrintWriter;

public class CommandPay extends ActionCommand {

    private Server server;

    public CommandPay(Domain domain, PrintWriter writer) {
        super(domain, writer);
        setServer();
    }

    private void setServer() {
        server = getDomain().getServer();
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
        if ("payed".equals(command)) {
            return true;
        }
        return false;
    }

    private void getPayed(String[] tokens) {
        PrintWriter writer = getWriter();
        if (tokens.length != 3) {
            writer.println(ERROR_MESSAGE);
        } else {
            String friend = tokens[2];
            double amount = Double.parseDouble(tokens[1]);
            String username = getDomain().getUsername();
            server.getFriendsList(username).get(friend).decrease(amount);
            server.getFriendsList(friend).get(username).increase(amount);
            sendMessageAfterPayed(writer, amount, friend, false);
        }
    }

    private void sendMessageAfterPayed(PrintWriter writer, double amount, String friend, boolean b) {

    }

}
