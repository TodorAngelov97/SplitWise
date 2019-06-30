package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;

import java.io.IOException;

public abstract class Command {

    public final int INDEX_OF_COMMAND = 0;
    public static final String ERROR_MESSAGE = "Wrong number of arguments.";
    private Domain domain;

    public Command(Domain domain) {
        this.domain = domain;
    }

    protected Domain getDomain() {
        return domain;
    }

    public abstract void executeCommand(String[] tokens) throws IOException;

    protected abstract boolean isMatched(String command);

}
