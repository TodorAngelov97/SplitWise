package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


//not ready
public class LoginCommand extends ActionCommand {

    private BufferedReader reader;
    private Server server;
    private PrintWriter writer;
    private String username;
    private Socket socket;

    public LoginCommand(Domain domain, PrintWriter writer) {
        super(domain, writer);
        server = getDomain().getServer();
        writer = getWriter();
        username = getDomain().getUsername();
        socket = getDomain().getSocket();
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
        return Commands.LOGIN.getCommand().equals(command);
    }

    private void login(String[] tokens) {

        loginWithCorrectData(tokens);
        server.addNewActiveUser(username, socket);

        PrintWriter writer = getWriter();
        writer.println("Successful login.");
        server.printUserNotifications(username, writer);
    }

    private void loginWithCorrectData(String[] tokens) {
        final int USERNAME_INDEX = 1;
        String inputUsername = tokens[USERNAME_INDEX];
        final int PASSWORD_INDEX = 2;
        String password = tokens[PASSWORD_INDEX];

        if (!isDataCorrect(inputUsername, password)) {
            repeatData();
        }
    }

    private boolean isDataCorrect(String username, String password) {
        return server.isLoggedIn(username, password, writer);
    }

    private void repeatData() {

        while (true) {
            try {
                String line = reader.readLine();
                String[] newTokens = line.split("\\s+");

                final int USERNAME_INDEX = 0;
                String inputUsername = newTokens[USERNAME_INDEX];
                final int PASSWORD_INDEX = 1;
                String password = newTokens[PASSWORD_INDEX];

                if (server.isLoggedIn(inputUsername, password, writer)) {
                    getDomain().setUsername(inputUsername);
                    break;
                }
            } catch (IOException e) {
                System.err.println("Exception thrown by ReadLine: " + e.getMessage());
            }
        }
    }
}
