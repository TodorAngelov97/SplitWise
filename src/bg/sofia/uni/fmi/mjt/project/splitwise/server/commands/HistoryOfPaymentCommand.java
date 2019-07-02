package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Commands;

import java.io.*;

import static bg.sofia.uni.fmi.mjt.project.splitwise.server.Server.SERVER_ERROR_MESSAGE;

public class HistoryOfPaymentCommand extends ActionCommand {

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
        try (FileReader fileReader = new FileReader(file);
             BufferedReader reader = new BufferedReader(fileReader)) {
            readInformation(reader);
        } catch (IOException e) {
            System.out.println(SERVER_ERROR_MESSAGE);
            System.err.println("Exception thrown by readLine: " + e.getMessage());
        }
    }

    private void readInformation(BufferedReader reader) throws IOException {
        while (true) {
            final String line = reader.readLine();
            if (line == null) {
                break;
            }
            writer.println(line);
        }
    }
}
