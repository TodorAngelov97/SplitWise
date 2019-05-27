package bg.sofia.uni.fmi.mjt.project.splitwise.client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;

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
				if (validateInput(tokens)) {
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
		try (BufferedReader readerOfHelpMessage = new BufferedReader(new FileReader(HELP_MESSAGE_FILE))) {

			String readLine;
			while ((readLine = readerOfHelpMessage.readLine()) != null) {
				System.out.println(readLine);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Problem with application, try again later.");
			System.err.println("Exception thrown by readLine: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Problem with application, try again later.");
			System.err.println("Exception thrown by createNewFile: " + e.getMessage());
		}

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

	private void connect(String[] tokens) {
		setStream();
		writer.println(String.join(" ", tokens));
		startNewThreadForPrinting();
		connected = true;

	}

	private void setStream() {
		try {
			writer = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			System.err.println("Error with open writer " + e.getMessage());
		}
	}

	private void startNewThreadForPrinting() {
		ClientRunnable clientRunnable = new ClientRunnable(socket);
		new Thread(clientRunnable).start();
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

	private void login(String[] tokens) {
		if (validateLogin(tokens)) {
			connect(tokens);
		} else {
			System.out.println("For login you need exactly 3 argumentsFor login you need exactly 3 arguments");
		}
	}

	private boolean validateLogin(String[] tokens) {
		return tokens.length == 3;
	}

	private boolean validateInput(String[] tokens) {
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
			Client c = new Client(new Socket("localhost", Server.PORT));
			c.execute();
		} catch (UnknownHostException e) {
			System.err.println("Exception thrown by Socket: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Exception thrown by Socket: " + e.getMessage());
		}

	}
}
