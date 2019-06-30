package bg.sofia.uni.fmi.mjt.project.splitwise.client;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    private static final String HELP_MESSAGE_FILE = "resources/help.txt";
    private Domain domain;

    public Client(Socket socket) {
        domain = new Domain(socket);
    }

    public void execute() {
        printHelpMessage();
        try (Scanner userInput = new Scanner(System.in)) {
            PrintWriter writer = domain.getWriter();
            while (true) {
                String input = userInput.nextLine();

                String[] tokens = input.split("\\s+");
                String command = tokens[0];
                if (isInputValid(tokens)) {
                    if (command.equals("sign-up")) {
                        signUp(tokens);
                    } else if (command.equals("login")) {
                        login(tokens);
                    } else if (command.equals("logout")) {
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

    private boolean isInputValid(String[] tokens) {
        for (String token : tokens) {
            if (token.equals(null)) {
                System.out.println("Wrong input");
                return false;
            }
        }
        return true;
    }


    private void signUp(String[] tokens) {
        if (isValidSignUpInputData(tokens)) {
            domain.connect(tokens);
        }
    }

    private boolean isValidSignUpInputData(String[] tokens) {

        if (tokens.length != 6) {
            System.out.println("For sign-up you need exactly 6 arguments");
            return false;
        }

        String password = tokens[2];
        String confirmationPassword = tokens[3];
        if (!password.equals(confirmationPassword)) {
            String message = "You have to insert same password.";
            System.out.println(message);
            return false;
        }
        return true;
    }

    private void login(String[] tokens) {
        if (isNumberOfArgumentsCorrect(tokens)) {
            domain.connect(tokens);
        } else {
            System.out.println("For login you need exactly 3 arguments");
        }
    }

    private boolean isNumberOfArgumentsCorrect(String[] tokens) {
        return tokens.length == 3;
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
