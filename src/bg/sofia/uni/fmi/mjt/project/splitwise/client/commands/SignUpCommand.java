package bg.sofia.uni.fmi.mjt.project.splitwise.client.commands;

import bg.sofia.uni.fmi.mjt.project.splitwise.client.utilitis.Domain;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Commands;

public class SignUpCommand extends ActionCommand {

    private Domain domain;

    public SignUpCommand(Domain domain) {
        super(domain);
        this.domain = getDomain();
    }

    @Override
    public void executeCommand(String[] tokens) {
        String command = tokens[INDEX_OF_COMMAND];
        if (isMatched(command)) {
            signUp(tokens);
        }
    }

    @Override
    protected boolean isMatched(String command) {
        return Commands.SIGN_UP.getCommand().equals(command);
    }

    private void signUp(String[] tokens) {
        if (isInputValid(tokens)) {
            domain.connect(tokens);
        }
    }

    private boolean isInputValid(String[] tokens) {

        if (isNumberOfArgumentsNotCorrect(tokens)) {
            final String MESSAGE = "For sign-up you need exactly 6 arguments";
            System.out.println(MESSAGE);
            return false;
        }

        final int INDEX_OF_PASSWORD = 2;
        String password = tokens[INDEX_OF_PASSWORD];

        final int INDEX_OF_CONFIRMATION = 3;
        String confirmationPassword = tokens[INDEX_OF_CONFIRMATION];
        if (arePasswordsNotCorrect(password, confirmationPassword)) {
            final String MESSAGE = "You have to insert same password.";
            System.out.println(MESSAGE);
            return false;
        }
        return true;
    }

    private boolean isNumberOfArgumentsNotCorrect(String[] tokens) {
        final int NUMBER_OF_ARGUMENTS = 6;
        return tokens.length != NUMBER_OF_ARGUMENTS;
    }

    private boolean arePasswordsNotCorrect(String password, String confirmationPassword) {
        return !(password.equals(confirmationPassword));
    }
}
