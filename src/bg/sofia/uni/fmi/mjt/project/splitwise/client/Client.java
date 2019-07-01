package bg.sofia.uni.fmi.mjt.project.splitwise.client;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.commands.Command;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Commands;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client {

    private static final String HELP_MESSAGE_FILE = "resources/help.txt";
    private Domain domain;
    private Map<String, Command> commands;

    public Client(Socket socket) {
        this.domain = new Domain(socket);
        this.commands = new HashMap<>();
    }

    public void execute() {
        printHelpMessage();
        try (Scanner userInput = new Scanner(System.in)) {
            executeCommands(userInput);
        } finally {
            domain.closeOpenResources();
        }
    }

    private void printHelpMessage() {
        try (FileReader fileReader = new FileReader(HELP_MESSAGE_FILE);
             BufferedReader reader = new BufferedReader(fileReader)) {
            printContentOfHelpFile(reader);
        } catch (FileNotFoundException e) {
            System.out.println("Problem with application, try again later.");
            System.err.println("Exception thrown by readLine: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Problem with application, try again later.");
            System.err.println("Exception thrown by createNewFile: " + e.getMessage());
        }
    }

    private void printContentOfHelpFile(BufferedReader reader) throws IOException {
        while (true) {
            final String line = reader.readLine();
            if (line == null) {
                break;
            }
            System.out.println(line);
        }
    }

    private void executeCommands(Scanner userInput) {
        initializeCommands();
        while (true) {
            String input = userInput.nextLine();
            if (isInputValid(input)) {
                executeSingleCommand(input);
            }
        }
    }

    private void initializeCommands() {
        commands.put(Commands.LOGIN.getCommand(), new LoginCommand(domain));
        commands.put(Commands.SIGN_UP.getCommand(), new SignUpCommand(domain));
    }

    private boolean isInputValid(String input) {
        String[] tokens = getTokensFromInput(input);
        for (String token : tokens) {
            if (token.equals(null)) {
                final String INVALID_INPUT = "Wrong input";
                System.out.println(INVALID_INPUT);
                return false;
            }
        }
        return true;
    }

    public static String[] getTokensFromInput(String input) {
        return input.split("\\s+");
    }

    private void executeSingleCommand(String input) {

        String[] tokens = getTokensFromInput(input);
        final int INDEX_OF_COMMAND = 0;
        String command = tokens[INDEX_OF_COMMAND];

        if (commands.containsKey(command)) {
            Command customCommand = commands.get(command);
            customCommand.executeCommand(tokens);
        } else if (domain.isConnected()) {
            PrintWriter writer = domain.getWriter();
            writer.println(input);
            checkIsLogout(command);
        }
    }

    private void checkIsLogout(String command) {
        if (Commands.LOGOUT.getCommand().equals(command)) {
            return;
        }
    }

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", Server.PORT);
            Client client = new Client(socket);
            client.execute();
        } catch (UnknownHostException e) {
            System.err.println("Exception thrown by Socket: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Exception thrown by Socket: " + e.getMessage());
        }
    }
}
