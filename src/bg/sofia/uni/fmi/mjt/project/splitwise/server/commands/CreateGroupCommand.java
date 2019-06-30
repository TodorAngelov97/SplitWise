package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Commands;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Group;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CreateGroupCommand extends Command {

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
        if (tokens.length >= 4) {
            create(tokens);
        } else {
            writer.println(ERROR_MESSAGE);
        }
    }

    //index;
    private void create(String[] tokens) {
        final int INDEX_GROUP = 1;
        final int BEGIN_OF_MEMBERS = 2;
        if (tokens.length < 4) {
            writer.println(ERROR_MESSAGE);
        } else {
            String nameOfGroup = tokens[INDEX_GROUP];
            List<String> friends = new ArrayList<>();
            for (int i = BEGIN_OF_MEMBERS; i < tokens.length; ++i) {
                friends.add(tokens[i]);
            }
            Group group = new Group(friends);
            server.addGroup(username, nameOfGroup, group);
            writer.printf(String.format("You created the group %s.%n", nameOfGroup));
            friends.add(username);
            for (int i = BEGIN_OF_MEMBERS; i < tokens.length; ++i) {
                List<String> newStr = new ArrayList<>(friends);
                newStr.remove(tokens[i]);
                server.addGroup(tokens[i], nameOfGroup, new Group(newStr));
                String message = String.format("* %s:%n%s created group with you.", nameOfGroup,
                        server.getProfileNames(username));
                server.sendGroupNotification(tokens[i], message);
            }
        }
    }
}
