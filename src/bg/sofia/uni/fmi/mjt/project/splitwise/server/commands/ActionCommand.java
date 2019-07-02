package bg.sofia.uni.fmi.mjt.project.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Domain;

public abstract class ActionCommand extends Command {

    public static final String ERROR_MESSAGE = "Wrong number of arguments.";
    private Domain domain;

    public ActionCommand(Domain domain) {
        this.domain = domain;
    }

    protected Domain getDomain() {
        return domain;
    }
}
