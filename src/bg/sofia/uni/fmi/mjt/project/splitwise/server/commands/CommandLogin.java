package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class CommandLogin extends ActionCommand {

    private BufferedReader reader;
    private Server server;

    public CommandLogin(Domain domain, PrintWriter writer) {
        super(domain, writer);
        setServer(domain);
    }

    private void setServer(Domain domain) {
        this.server = domain.getServer();
    }

    @Override
    public void executeCommand(String[] tokens) {
        String command = tokens[INDEX_OF_COMMAND];
        if (isMatched(command)) {
            login(tokens);
        }
    }

    @Override
    protected boolean isMatched(String command) {
        if ("login".equals(command)) {
            return true;
        }
        return false;
    }

    private void login(String[] tokens) {

        loginWithCorrectData(tokens);
        String username = getDomain().getUsername();
        Socket socket = getDomain().getSocket();
        server.addNewActiveUser(username, socket);

        PrintWriter writer = getWriter();
        writer.println("Successful login.");
        server.printUserNotifications(writer, username);
    }

    private void loginWithCorrectData(String[] tokens) {
        final int USERNAME_INDEX = 1;
        String username = tokens[USERNAME_INDEX];
        final int PASSWORD_INDEX = 2;
        String password = tokens[PASSWORD_INDEX];

        if (!isDataCorrect(username, password)) {
            repeatData();
        }
    }

    private boolean isDataCorrect(String username, String password) {
        PrintWriter writer = getWriter();
        return server.isLoggedIn(username, password, writer);
    }

    private void repeatData() {

        while (true) {
            try {
                String line = reader.readLine();
                String[] newTokens = line.split("\\s+");

                final int USERNAME_INDEX = 0;
                String username = newTokens[USERNAME_INDEX];
                final int PASSWORD_INDEX = 1;
                String password = newTokens[PASSWORD_INDEX];

                PrintWriter writer = getWriter();
                if (server.isLoggedIn(username, password, writer)) {
                    getDomain().setUsername(username);
                    break;
                }
            } catch (IOException e) {
                System.err.println("Exception thrown by ReadLine: " + e.getMessage());
            }
        }
    }
}
