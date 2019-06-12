package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;

import java.io.PrintWriter;

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

    //improve logic
    private void create(String[] tokens) {
//
//        String nameOfTheGroup = tokens[1];
//        List<String> friends = new ArrayList<>();
//        for (int i = 2; i < tokens.length; ++i) {
//            friends.add(tokens[i]);
//        }
//        Group group = new Group(friends);
//        server.getGroupsOfUser(username).put(nameOfTheGroup, group);
//
//        writer.printf(String.format("You created the group %s.%n", nameOfTheGroup));
//
//        friends.add(username);
//        for (int i = 2; i < tokens.length; ++i) {
//            List<String> newStr = new ArrayList<>(friends);
//            newStr.remove(tokens[i]);
//            server.getGroupsOfUser(tokens[i]).put(nameOfTheGroup, new Group(newStr));
//
//            String message = String.format("* %s:%n%s created group with you.", nameOfTheGroup,
//                    server.getProfileNames(username));
//            sendGroupNotification(tokens[i], message);
//        }
    }
}
