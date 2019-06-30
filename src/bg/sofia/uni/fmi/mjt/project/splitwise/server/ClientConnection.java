package bg.sofia.uni.fmi.mjt.project.splitwise.server;

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
    private static final int MINUS = -1;
    private static final String NOTIFICATION = "*Notification*";
    private static final String ERROR_MESSAGE = "Wrong number of arguments.";
    private Socket socket;
    private String currency;
    private Domain domain;
    private Server server;
    private Map<String, Command> commands;

    public ClientConnection(Socket socket, Server server) {

        this.socket = socket;
        this.domain = new Domain(server, socket);
        currency = Currencies.BGN.getCurrency();
        this.server = server;
        commands = new HashMap<>();
    }


    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            initializeCommands(writer, reader);
            while (true) {

                String commandInput = reader.readLine();
                if (commandInput != null) {

                    String[] tokens = commandInput.split("\\s+");

                    String command = tokens[0];
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

    private void initializeCommands(PrintWriter writer, BufferedReader reader) {

        commands.put(Commands.ADD.getCommand(), new AddFriendCommand(domain, writer));
        commands.put(Commands.CREATE.getCommand(), new CreateGroupCommand(domain, writer));
        commands.put(Commands.GET_STATUS.getCommand(), new GetStatusCommand(domain, writer));
        commands.put(Commands.HISTORY_OF_PAYMENT.getCommand(), new HistoryOfPaymentCommand(domain, writer));
        commands.put(Commands.LOGIN.getCommand(), new LoginCommand(domain, writer));
        commands.put(Commands.PAYED.getCommand(), new PayCommand(domain, writer));
        commands.put(Commands.PAYED_GROUP.getCommand(), new PayedGroupCommand(domain, writer));
        commands.put(Commands.SIGN_UP.getCommand(), new SignUpCommand(domain, writer, reader));
        commands.put(Commands.SPLIT.getCommand(), new SplitMoneyCommand(domain, writer));
        commands.put(Commands.SPLIT_GROUP.getCommand(), new SplitGroupMoneyCommand(domain, writer));
    }

    private void execute() {

    }

}
