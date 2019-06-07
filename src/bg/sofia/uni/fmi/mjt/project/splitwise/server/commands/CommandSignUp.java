package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.UserProfile;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.ServerOld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class CommandSignUp extends ActionCommand {

    private BufferedReader reader;

    //isCorrect?
    private Server server;

    public CommandSignUp(Domain domain, PrintWriter writer, BufferedReader reader) {
        super(domain, writer);
        this.reader = reader;
        setServer(domain);
    }

    private void setServer(Domain domain) {
        server = domain.getServer();
    }

    @Override
    protected boolean isMatched(String command) {
        if ("sign-up".equals(command)) {
            return true;
        }
        return false;
    }

    @Override
    public void executeCommand(String[] tokens) {
        String command = tokens[INDEX_OF_COMMAND];
        if (isMatched(command)) {
            signUp(tokens);
        }
    }

    private void signUp(String[] tokens) {
        setCorrectUsername(tokens);
        addNewUser(tokens);

        String username = getDomain().getUsername();
        Socket socket = getDomain().getSocket();
        server.addNewActiveUser(username, socket);
        server.saveUserInFile();

        PrintWriter writer = getWriter();
        writer.println("Successful sign-up.");
    }

    private void setCorrectUsername(String[] tokens) {

        final int USERNAME_INDEX = 1;
        String username = tokens[USERNAME_INDEX];
        if (isUsernameNotContained(username)) {
            this.getDomain().setUsername(username);
        } else {
            repeatUsername();
        }
    }

    private boolean isUsernameNotContained(String username) {
        return !(server.isUsernameContained(username));
    }

    private void repeatUsername() {
        while (true) {
            String username;
            try {
                username = reader.readLine();
                if (isUsernameNotContained(username)) {
                    this.getDomain().setUsername(username);
                    break;
                }
            } catch (IOException e) {
                System.err.println("Error when reading line. " + e.getMessage());
            }
        }
    }

    private void addNewUser(String[] tokens) {

        final int PASSWORD_INDEX = 2;
        String password = tokens[PASSWORD_INDEX];

        final int FIRST_NAME_INDEX = 4;
        String firstName = tokens[FIRST_NAME_INDEX];

        final int LAST_NAME_INDEX = 5;
        String lastName = tokens[LAST_NAME_INDEX];
        String username = getDomain().getUsername();

        server.addUser(username, new UserProfile(username, password, firstName, lastName));
    }
}
