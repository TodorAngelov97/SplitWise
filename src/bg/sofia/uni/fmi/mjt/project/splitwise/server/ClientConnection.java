package bg.sofia.uni.fmi.mjt.project.splitwise.server;

import bg.sofia.uni.fmi.mjt.project.splitwise.client.Client;
import bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler.Currencies;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.commands.*;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientConnection implements Runnable {
    private Socket socket;
    private String currency;//not used with particular point
    private Domain domain;
    private Server server;
    private Map<String, Command> commands;

    public ClientConnection(Socket socket, Server server) {
        this.socket = socket;
        this.domain = new Domain(server, socket);
        this.currency = Currencies.BGN.getCurrency();
        this.server = server;
        commands = new HashMap<>();
    }

    @Override
    public void run() {
        try (InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
             BufferedReader reader = new BufferedReader(inputStreamReader);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
            executeCommands(reader, writer);
        } catch (IOException e) {
            System.out.println("socket is closed");
            System.out.println(e.getMessage());
        } finally {
            String username = domain.getUsername();
            server.removeUser(username);
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error with closing socket" + e.getMessage());
            }
        }
    }

    private void initializeCommands(BufferedReader reader) {
        commands.put(Commands.ADD.getCommand(), new AddFriendCommand(domain));
        commands.put(Commands.CREATE.getCommand(), new CreateGroupCommand(domain));
        commands.put(Commands.GET_STATUS.getCommand(), new GetStatusCommand(domain));
        commands.put(Commands.HISTORY_OF_PAYMENT.getCommand(), new HistoryOfPaymentCommand(domain));
        commands.put(Commands.LOGIN.getCommand(), new LoginCommand(domain, reader));
        commands.put(Commands.PAYED.getCommand(), new PayCommand(domain));
        commands.put(Commands.PAYED_GROUP.getCommand(), new PayedGroupCommand(domain));
        commands.put(Commands.SIGN_UP.getCommand(), new SignUpCommand(domain, reader));
        commands.put(Commands.SPLIT.getCommand(), new SplitMoneyCommand(domain));
        commands.put(Commands.SPLIT_GROUP.getCommand(), new SplitGroupMoneyCommand(domain));
    }

    private void executeCommands(BufferedReader reader, PrintWriter writer) throws IOException {
        domain.setWriter(writer);
        initializeCommands(reader);
        while (true) {
            String commandInput = reader.readLine();
            if (commandInput != null) {
                executeCommand(commandInput, writer);
            }
        }
    }

    private void executeCommand(String commandInput, PrintWriter writer) {
        String[] tokens = Client.getTokensFromInput(commandInput);
        final int INDEX_OF_COMMAND = 0;
        String command = tokens[INDEX_OF_COMMAND];
        if (commands.containsKey(command)) {
            Command customCommand = commands.get(command);
            customCommand.executeCommand(tokens);
        } else if (Commands.LOGOUT.getCommand().equals(command)) {
            return;
        } else {
            writer.println("Wrong command, try again.");
        }
    }
}
