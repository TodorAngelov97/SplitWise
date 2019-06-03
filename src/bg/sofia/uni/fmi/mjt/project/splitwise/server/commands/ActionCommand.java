package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;

import java.io.PrintWriter;

public abstract class ActionCommand extends Command {
    private PrintWriter writer;

    public ActionCommand(Domain domain, PrintWriter writer) {
        super(domain);
        this.writer = writer;
    }

    public PrintWriter getWriter() {
        return writer;
    }
}
