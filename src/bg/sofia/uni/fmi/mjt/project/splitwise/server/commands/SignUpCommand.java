package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Commands;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.UserProfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SignUpCommand extends Command {

    private Server server;
    private PrintWriter writer;
    private BufferedReader reader;


    public SignUpCommand(Domain domain, BufferedReader reader) {
        super(domain);
        server = domain.getServer();
        writer = getDomain().getWriter();
        this.reader = reader;
    }


    @Override
    protected boolean isMatched(String command) {
        return Commands.SIGN_UP.getCommand().equals(command);
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

        UserProfile newUserProfile = new UserProfile(username, password, firstName, lastName);
        server.addUser(username, newUserProfile);
    }
}
