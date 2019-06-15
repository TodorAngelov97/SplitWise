package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Group;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CommandCreateGroup extends ActionCommand {
    public CommandCreateGroup(Domain domain, PrintWriter writer) {
        super(domain, writer);
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
        if ("create-group".equals(command)) {
            return true;
        }
        return false;
    }

    private void createGroup(String[] tokens) {
        if (tokens.length >= 4) {
            create(tokens);
        } else {
            PrintWriter writer = getWriter();
            writer.println(ERROR_MESSAGE);
        }
    }

    private void create(String[] tokens) {
        PrintWriter writer = getWriter();
        if (tokens.length < 4) {
            writer.println(ERROR_MESSAGE);
        } else {
            String nameOfGroup = tokens[1];
            List<String> friends = new ArrayList<>();
            for (int i = 2; i < tokens.length; ++i) {
                friends.add(tokens[i]);
            }
            Group group = new Group(friends);
            Server server = getDomain().getServer();
            String username = getDomain().getUsername();
            server.addGroup(username, nameOfGroup, group);
            writer.printf(String.format("You created the group %s.%n", nameOfGroup));
            friends.add(username);
            for (int i = 2; i < tokens.length; ++i) {
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
