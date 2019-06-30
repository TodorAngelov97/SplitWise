package bg.sofia.uni.fmi.mjt.project.splitwise.client;

import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Commands;

public class SignUpCommand extends ActionCommand {

    public final int INDEX_OF_COMMAND = 0;

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

    private void signUp(String[] tokens) {
        if (isInputValid(tokens)) {
            domain.connect(tokens);
        }
    }

    private boolean isInputValid(String[] tokens) {

        if (isNotCorrectNumberOfArguments(tokens)) {
            String message = "For sign-up you need exactly 6 arguments";
            System.out.println(message);
            return false;
        }

        String password = tokens[2];
        String confirmationPassword = tokens[3];
        if (areNotCorrectPasswords(password, confirmationPassword)) {
            String message = "You have to insert same password.";
            System.out.println(message);
            return false;
        }
        return true;
    }

    private boolean isNotCorrectNumberOfArguments(String[] tokens) {
        final int NUMBER_OF_ARGUMENTS = 6;
        return tokens.length != NUMBER_OF_ARGUMENTS;
    }

    private boolean areNotCorrectPasswords(String password, String confirmationPassword) {
        return !(password.equals(confirmationPassword));
    }

    @Override
    protected boolean isMatched(String command) {
        return Commands.SIGN_UP.getCommand().equals(command);
    }
}
