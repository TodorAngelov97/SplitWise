package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

public abstract class Command {

     protected abstract boolean isMatched();

     public abstract void executeCommand();
}
