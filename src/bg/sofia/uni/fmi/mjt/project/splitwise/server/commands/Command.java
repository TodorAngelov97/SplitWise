package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

public abstract class Command {
    public final int INDEX_OF_COMMAND = 0;

    public abstract void executeCommand(String[] tokens);

    protected abstract boolean isMatched(String command);

}
