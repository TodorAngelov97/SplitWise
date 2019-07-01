package bg.sofia.uni.fmi.mjt.project.splitwise.client.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.client.utilitis.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Commands;

public class LoginCommand extends ActionCommand {

    private Domain domain;

    public LoginCommand(Domain domain) {
        super(domain);
        this.domain = getDomain();
    }

    @Override

    public void executeCommand(String[] tokens) {
        String command = tokens[INDEX_OF_COMMAND];
        if (isMatched(command)) {
            login(tokens);
        }
    }

    @Override
    protected boolean isMatched(String command) {
        return Commands.LOGIN.getCommand().equals(command);
    }

    private void login(String[] tokens) {
        if (isNumberOfArgumentsCorrect(tokens)) {
            domain.connect(tokens);
        } else {
            final String MESSAGE = "For login you need exactly 3 arguments";
            System.out.println(MESSAGE);
        }
    }

    private boolean isNumberOfArgumentsCorrect(String[] tokens) {
        final int NUMBER_OF_ARGUMENTS = 3;
        return tokens.length == NUMBER_OF_ARGUMENTS;
    }

}
