package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import static bg.sofia.uni.fmi.mjt.project.splitwise.server.Server.SERVER_ERROR_MESSAGE;

public class LoginCommand extends ActionCommand {

    private Server server;
    private PrintWriter writer;
    private String username;
    private Socket socket;
    private BufferedReader reader;

    public LoginCommand(Domain domain, BufferedReader reader) {
        super(domain);
        server = getDomain().getServer();
        writer = getDomain().getWriter();
        username = getDomain().getUsername();
        socket = getDomain().getSocket();
        this.reader = reader;
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
        final String MESSAGE = "Successful login.";
        writer.println(MESSAGE);
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
                System.out.println(SERVER_ERROR_MESSAGE);
                System.err.println("Exception thrown by ReadLine: " + e.getMessage());
            }
        }
    }
}
