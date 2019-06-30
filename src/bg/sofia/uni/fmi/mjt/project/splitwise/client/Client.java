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
            PrintWriter writer = domain.getWriter();
            initializeCommands();
            while (true) {
                String input = userInput.nextLine();

                String[] tokens = input.split("\\s+");
                String command = tokens[0];

                if (isInputValid(tokens)) {
                    if (commands.containsKey(command)) {
                        Command customCommand = commands.get(command);
                        customCommand.executeCommand(tokens);
                    } else if (Commands.LOGOUT.getCommand().equals(command)) {
                        writer.println(input);
                        return;
                    } else if (domain.isConnected()) {
                        writer.println(input);
                    }
                }
            }
        } finally {
            domain.closeOpenResources();
        }
    }

    private void printHelpMessage() {
        try (FileReader fileReader = new FileReader(HELP_MESSAGE_FILE);
             BufferedReader readerOfHelpMessage = new BufferedReader(fileReader)) {
            while (true) {
                final String line = readerOfHelpMessage.readLine();
                if (line == null) {
                    break;
                }
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Problem with application, try again later.");
            System.err.println("Exception thrown by readLine: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Problem with application, try again later.");
            System.err.println("Exception thrown by createNewFile: " + e.getMessage());
        }
    }

    private void initializeCommands() {
        commands.put(Commands.LOGIN.getCommand(), new LoginCommand(domain));
        commands.put(Commands.SIGN_UP.getCommand(), new SignUpCommand(domain));
    }

    private boolean isInputValid(String[] tokens) {
        for (String token : tokens) {
            if (token.equals(null)) {
                System.out.println("Wrong input");
                return false;
            }
        }
        return true;
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
