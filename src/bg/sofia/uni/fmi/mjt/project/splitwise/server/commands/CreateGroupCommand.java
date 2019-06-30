package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Commands;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Group;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CreateGroupCommand extends ActionCommand {

    private Server server;
    private PrintWriter writer;
    private String username;

    public CreateGroupCommand(Domain domain) {
        super(domain);
        server = getDomain().getServer();
        writer = getDomain().getWriter();
        username = getDomain().getUsername();
    }

    @Override
    public void executeCommand(String[] tokens) {
        String command = tokens[INDEX_OF_COMMAND];
        if (isMatched(command)) {
            createGroup(tokens);
        }
    }

    @Override
    protected boolean isMatched(String command) {
        return Commands.CREATE.getCommand().equals(command);
    }

    private void createGroup(String[] tokens) {
        final int NUMBER_OF_ARGUMENTS = 4;
        if (tokens.length >= NUMBER_OF_ARGUMENTS) {
            create(tokens);
        } else {
            writer.println(ERROR_MESSAGE);
        }
    }

    private void create(String[] tokens) {
        final int INDEX_GROUP = 1;
        final int BEGIN_OF_MEMBERS = 2;
        String nameOfGroup = tokens[INDEX_GROUP];
        List<String> friends = new ArrayList<>();

        for (int i = BEGIN_OF_MEMBERS; i < tokens.length; ++i) {
            friends.add(tokens[i]);
        }

        Group group = new Group(friends);
        server.addGroup(username, nameOfGroup, group);
        String message = String.format("You created the group %s.%n", nameOfGroup);
        writer.printf(message);
        friends.add(username);

        for (int i = BEGIN_OF_MEMBERS; i < tokens.length; ++i) {
            List<String> newStr = new ArrayList<>(friends);
            newStr.remove(tokens[i]);
            group = new Group(newStr);
            server.addGroup(tokens[i], nameOfGroup, group);
            message = String.format("* %s:%n%s created group with you.", nameOfGroup,
                    server.getProfileNames(username));
            server.sendGroupNotification(tokens[i], message);
        }
    }
}
