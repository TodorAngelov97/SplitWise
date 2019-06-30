package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import java.io.IOException;

public abstract class Command {
    public abstract void executeCommand(String[] tokens) throws IOException;

    protected abstract boolean isMatched(String command);

}
