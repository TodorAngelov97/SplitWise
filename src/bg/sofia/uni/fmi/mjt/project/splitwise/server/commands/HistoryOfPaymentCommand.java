package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Commands;

import java.io.*;

public class HistoryOfPaymentCommand extends Command {

    private Server server;
    private PrintWriter writer;
    private String username;

    public HistoryOfPaymentCommand(Domain domain) {
        super(domain);
        server = getDomain().getServer();
        writer = getDomain().getWriter();
        username = getDomain().getUsername();
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
        return Commands.HISTORY_OF_PAYMENT.equals(command);
    }

    private void getHistoryOfPayment() {

        File file = server.getFile(username);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (true) {
                final String line = reader.readLine();
                if (line == null) {
                    break;
                }
                writer.println(line);
            }
        } catch (IOException e) {
            System.err.println("Exception thrown by readLine: " + e.getMessage());
        }
    }
}
