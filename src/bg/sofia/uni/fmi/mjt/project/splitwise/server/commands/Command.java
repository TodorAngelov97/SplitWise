package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

public abstract class Command {
    public abstract void executeCommand(String[] tokens);

    protected abstract boolean isMatched(String command);

}
