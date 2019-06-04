package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;

import java.io.*;

public class CommandHistoryOfPayment extends ActionCommand {


    public CommandHistoryOfPayment(Domain domain, PrintWriter writer) {
        super(domain, writer);
    }

    @Override
    public void executeCommand(String[] tokens) {
        String command = tokens[INDEX_OF_COMMAND];
        if (isMatched(command)) {
            getHistoryOfPayment();
        }
    }

    @Override
    protected boolean isMatched(String command) {
        if ("history-of-payment".equals(command)) {
            return true;
        }
        return false;
    }

    private void getHistoryOfPayment() {

        Server server = getDomain().getServer();
        String username = getDomain().getUsername();
        File file = server.getFile(username);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (true) {
                final String line = reader.readLine();
                if (line == null) {
                    break;
                }
                PrintWriter writer = getWriter();
                writer.println(line);
            }
        } catch (IOException e) {
            System.err.println("Exception thrown by readLine: " + e.getMessage());
        }
    }
}
