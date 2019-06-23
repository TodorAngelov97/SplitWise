package bg.sofia.uni.fmi.mjt.project.splitwise.client;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.ServerOld;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    private static final String HELP_MESSAGE_FILE = "resources/help.txt";
    private PrintWriter writer;
    private Socket socket;
    private boolean connected;

    public Client(Socket socket) {
        this.socket = socket;
        this.writer = null;
        this.connected = false;
    }

    public void execute() {
        printHelpMessage();
        try (Scanner userInput = new Scanner(System.in)) {
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
                    } else if (connected) {
                        writer.println(input);
                    }
                }
            }
        } finally {
            closeOpenResources();
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
                writer.println(line);
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

    private void closeOpenResources() {
        try {
            if (writer != null) {
                writer.close();
            }
            socket.close();
        } catch (IOException e) {
            System.err.println("Error with closing writer stream" + e.getMessage());
        }
    }

    private void signUp(String[] tokens) {
        if (isValidSignUpInputData(tokens)) {
            connect(tokens);
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

    private void connect(String[] tokens) {
        setStream();
        writer.println(String.join(" ", tokens));
        turnOnListenerThread();
        connected = true;

    }

    private void setStream() {
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("Error with open writer " + e.getMessage());
        }
    }

    private void turnOnListenerThread() {
        ClientRunnable clientRunnable = new ClientRunnable(socket);
        new Thread(clientRunnable).start();
    }

    private void login(String[] tokens) {
        if (isNumberOfArgumentsCorrect(tokens)) {
            connect(tokens);
        } else {
            System.out.println("For login you need exactly 3 arguments");
        }
    }

    private boolean isNumberOfArgumentsCorrect(String[] tokens) {
        return tokens.length == 3;
    }

    
    public static void main(String[] args) {
        try {
            Client client = new Client(new Socket("localhost", ServerOld.PORT));
            client.execute();
        } catch (UnknownHostException e) {
            System.err.println("Exception thrown by Socket: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Exception thrown by Socket: " + e.getMessage());
        }
    }
}
