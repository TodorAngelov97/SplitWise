package bg.sofia.uni.fmi.mjt.project.splitwise.client;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.commands.Command;

public abstract class ActionCommand extends Command {

    private Domain domain;

    public ActionCommand(Domain domain) {
        this.domain = domain;
    }

    protected Domain getDomain() {
        return domain;
    }
}
