package bg.sofia.uni.fmi.mjt.project.splitwise.server;

<<<<<<< HEAD
import bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler.Currencies;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.commands.*;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Commands;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Friend;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Group;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.UserProfile;

import java.io.*;
import java.net.Socket;
import java.util.*;
=======
import bg.sofia.uni.fmi.mjt.project.splitwise.Friend;
import bg.sofia.uni.fmi.mjt.project.splitwise.Group;
import bg.sofia.uni.fmi.mjt.project.splitwise.UserProfile;
import bg.sofia.uni.fmi.mjt.project.splitwise.ratehandler.RateHandler;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
>>>>>>> master

public class ClientConnection implements Runnable {
    private static final int MINUS = -1;
    private static final String NOTIFICATION = "*Notification*";
    private static final String ERROR_MESSAGE = "Wrong number of arguments.";
<<<<<<< HEAD
    private Socket socket;
    private String currency;
    //	private double rate;
    private Domain domain;
    private String username;
    private Server server;
    private Map<String, Command> commands;
=======
    private String username;
    private Socket socket;
    private String currency;
    private double rate;
    private Server server;
>>>>>>> master

    public ClientConnection(Socket socket, Server server) {

        this.socket = socket;
<<<<<<< HEAD
        this.domain = new Domain(server, socket);
        currency = Currencies.BGN.getCurrency(); ///
//		rate = 1.0;
        this.server = server;
        commands = new HashMap<>();
    }


    //one more method for while true;
=======
        this.server = server;
        currency = "BGN";
        rate = 1.0;
    }

>>>>>>> master
    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

<<<<<<< HEAD
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
/*
=======
            while (true) {
                String commandInput = reader.readLine();

                if (commandInput != null) {
                    String[] tokens = commandInput.split("\\s+");
                    String command = tokens[0];

>>>>>>> master
                    if ("sign-up".equals(command)) {
                        signUp(writer, tokens, reader);
                    } else if ("login".equals(command)) {
                        login(writer, tokens, reader);
                    } else if ("add-friend".equals(command)) {
                        addFriend(writer, tokens);
                    } else if ("create-group".equals(command)) {
                        createGroup(writer, tokens);
                    } else if ("split".equals(command)) {
                        splitMoney(writer, tokens);
                    } else if ("split-group".equals(command)) {
                        splitMoneyGroup(writer, tokens);
                    } else if ("get-status".equals(command)) {
                        getStatus(writer);
                    } else if ("payed".equals(command)) {
                        payed(writer, tokens);
                    } else if ("payed-group".equals(command)) {
                        payedGroup(writer, tokens);
<<<<<<< HEAD
//                    } else if ("switch-currency".equals(command)) {
//                        switchCurrency(writer, tokens);
=======
                    } else if ("switch-currency".equals(command)) {
                        switchCurrency(writer, tokens);
>>>>>>> master
                    } else if ("history-of-payment".equals(command)) {
                        historyOfPayment(writer);
                    } else if ("logout".equals(command)) {
                        return;
                    } else {
                        writer.println("Wrong command, try again.");
                    }
<<<<<<< HEAD
  */
=======
>>>>>>> master
                }
            }
        } catch (IOException e) {
            System.out.println("socket is closed");
            System.out.println(e.getMessage());
        } finally {
            server.removeUser(username);
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error with closing socket" + e.getMessage());
<<<<<<< HEAD
=======

>>>>>>> master
            }
        }
    }

<<<<<<< HEAD
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

=======
>>>>>>> master
    private void signUp(PrintWriter writer, String[] tokens, BufferedReader reader) {
        if (!server.isUsernameContained(tokens[1])) {
            this.username = tokens[1];
        } else {
            while (true) {
                String line;
                try {
                    line = reader.readLine();
                    if (!server.isUsernameContained(line)) {
                        this.username = line;
                        break;
                    }
                } catch (IOException e) {
                    System.err.println("Error when reading line. " + e.getMessage());
                }
            }
        }
        String password = tokens[2];

        String firstName = tokens[4];
        String lastName = tokens[5];
<<<<<<< HEAD
        server.addUser(username, new UserProfile(username, password, firstName, lastName));
=======
        server.addNewUser(username, new UserProfile(username, password, firstName, lastName));
>>>>>>> master
        server.addNewActiveUser(username, socket);
        server.saveUserInFile();
        writer.println("Successful sign-up.");
    }

    private void login(PrintWriter writer, String[] tokens, BufferedReader reader) {
        this.username = tokens[1];
        String password = tokens[2];
        if (!server.isLoggedIn(username, password, writer)) {
            while (true) {
                try {
                    String line = reader.readLine();
                    String[] newTokens = line.split("\\s+");
                    if (server.isLoggedIn(newTokens[0], newTokens[1], writer)) {
                        break;
                    }
                } catch (IOException e) {
                    System.err.println("Exception thrown by ReadLine: " + e.getMessage());
                }
            }

        }

        server.addNewActiveUser(username, socket);
        writer.println("Successful login.");
<<<<<<< HEAD
        server.printUserNotifications(username, writer);
=======
        server.printUserNotifications(writer, username);
>>>>>>> master

    }

    private void addFriend(PrintWriter writer, String[] tokens) {
        if (tokens.length == 2) {
            String friend = tokens[1];
            if (!server.isUsernameContained(friend)) {
                writer.println(String.format("User with name: %s does not exists.", friend));
<<<<<<< HEAD
            } else if (server.isUserInFriends(username, friend)) {
=======
            } else if (server.getFriendsList(username).containsKey(friend)) {
>>>>>>> master
                writer.println(String.format("User with name: %s already in your friend list.", friend));
            } else if (username.equals(friend)) {
                writer.println("You can not add yourself as a friend.");

            } else {
<<<<<<< HEAD
                server.addFriend(username, tokens[1]);
                server.addFriend(tokens[1], username);
//                server.getFriendsList(username).put(tokens[1], new Friend());
//                server.getFriendsList(friend).put(username, new Friend());
=======
                server.getFriendsList(username).put(tokens[1], new Friend());
                server.getFriendsList(friend).put(username, new Friend());
>>>>>>> master
                writer.println("Successfully added friend with name: " + friend);
                String message = String.format("%s added you as friend. %n", server.getProfileNames(username));
                sendFriendNotification(friend, message);
            }
        } else {
            writer.println(ERROR_MESSAGE);
        }
    }

    private void splitMoney(PrintWriter writer, String[] tokens) {
        if (tokens.length == 4) {
            String friend = tokens[2];
            if (!server.isUsernameContained(friend)) {
                writer.println(String.format("User with name %s does not exists.", friend));
                return;
<<<<<<< HEAD
            } else if (!server.isUserInFriends(username, friend)) {
=======
            } else if (!server.getFriendsList(username).containsKey(friend)) {
>>>>>>> master
                writer.println(String.format(
                        "This user %s is not in your friend list, you have to added before splitting money.", friend));
                return;
            }

            double amount = Double.parseDouble(tokens[1]) / 2;
<<<<<<< HEAD
            // double exchangeRate = getExchangeRate(server.getRate(friend));
//			server.getFriendsList(username).get(friend).increase(amount * 1 / rate);
//			server.getFriendsList(friend).get(username).decrease(amount * 1 / rate);
            server.increaseAmountOfFriend(username, friend, amount);
            server.decreaseAmountOfFriend(friend, username, amount);

//            String paymentMessage = String.format("Split %s %s between you and %s for %s.%n", amount, currency,
//                    friend, tokens[3]);

            String paymentMessage = String.format("Split %s  between you and %s for %s.%n", amount,
                    friend, tokens[3]);

=======
            server.getFriendsList(username).get(friend).increase(amount * 1 / rate);
            server.getFriendsList(friend).get(username).decrease(amount * 1 / rate);
            String paymentMessage = String.format("Split %s %s between you and %s for %s.%n", amount, currency,
                    friend, tokens[3]);
>>>>>>> master
            writer.printf(paymentMessage);
            writeInPaymentFile(paymentMessage);
            StringBuilder message = new StringBuilder();
            message.append("Current status: ");
<<<<<<< HEAD
            // da go provers
//            getStatusForOneClient(message, server.getFriendsList(username).get(friend).getAmount() * rate);
            getStatusForOneClient(message, server.getFriendAmount(username, friend));

            writer.println(message.toString());
            String reasonForPayment = tokens[3];
//            String friendMessage = String.format("You owe %s %s %s %s %n", server.getProfileNames(username), amount,
//                    currency, reasonForPayment);
            String friendMessage = String.format("You owe  %s %s %s %n", server.getProfileNames(username), amount,
                    reasonForPayment);

=======
            getStatusForOneClient(message, server.getFriendsList(username).get(friend).getAmount() * rate);
            writer.println(message.toString());
            String reasonForPayment = tokens[3];
            String friendMessage = String.format("You owe %s %s %s %s %n", server.getProfileNames(username), amount,
                    currency, reasonForPayment);
>>>>>>> master
            sendFriendNotification(friend, friendMessage);
        } else {
            writer.println(ERROR_MESSAGE);
        }
    }

    private void splitMoneyGroup(PrintWriter writer, String[] tokens) throws IOException {
        if (tokens.length == 4) {
            String group = tokens[2];
<<<<<<< HEAD
            int membersCount = server.getNumberOfMembersInGroup(username, group);
            double initialSum = Double.parseDouble(tokens[1]);
            double amount = initialSum / membersCount;


            for (String friend : server.getMembersNamesInGroup(username, tokens[2])) {
                server.decreaseAmountOfGroupMember(friend, group, username, amount);
                server.increaseAmountOfGroupMember(username, group, friend, amount);
            }

//            String paymentMessage = String.format("Split %s %s between you and group %s.%n", initialSum, currency,
//                    group);

            String paymentMessage = String.format("Split %s  between you and group %s.%n", initialSum,
                    group);

            writer.printf(paymentMessage);
            writeInPaymentFile(paymentMessage);
            String reasonForPayment = tokens[3];
            for (String memberOfTheGroup : server.getMembersNamesInGroup(username, tokens[2])) {
//                String message = String.format("* %s:%nYou owe %s %s %s %s", group, server.getProfileNames(username),
//                        amount, currency, reasonForPayment);

                String message = String.format("* %s:%nYou owe %s  %s %s", group, server.getProfileNames(username),
                        amount, reasonForPayment);
=======
            int membersCount = server.getGroupsOfUser(username).get(group).getNumberOfMembers();
            double initialSum = Double.parseDouble(tokens[1]);
            double amount = initialSum / membersCount;

            for (String friend : server.getGroupsOfUser(username).get(tokens[2]).getAllNamesOfMembers()) {
                server.getGroupsOfUser(friend).get(group).decreaseAmountOfFriend(username, amount);
                server.getGroupsOfUser(username).get(group).increaseAmountOfFriend(friend, amount);
            }
            String paymentMessage = String.format("Split %s %s between you and group %s.%n", initialSum, currency,
                    group);
            writer.printf(paymentMessage);
            writeInPaymentFile(paymentMessage);
            String reasonForPayment = tokens[3];
            for (String memberOfTheGroup : server.getGroupsOfUser(username).get(group).getAllNamesOfMembers()) {
                String message = String.format("* %s:%nYou owe %s %s %s %s", group, server.getProfileNames(username),
                        amount, currency, reasonForPayment);
>>>>>>> master
                sendGroupNotification(memberOfTheGroup, message);
            }
        } else {
            writer.println(ERROR_MESSAGE);

        }
    }

    private void getStatus(PrintWriter writer) {
<<<<<<< HEAD
        if (server.hasNotFriends(username) && server.hasNotGroups(username)) {
            writer.println("You don't have any added friends and groups");
            return;
        }
        if (!server.hasNotFriends(username)) {
            writer.println("Friends:");
            getStatusForFriends(server.getFriends(username).entrySet(), writer);
        }
        if (!server.hasNotGroups(username)) {
            writer.println("Groups:");
            getStatusForGroups(server.getGroups(username).entrySet(), writer);
=======
        if (server.getFriendsList(username).isEmpty() && server.getGroupsOfUser(username).isEmpty()) {
            writer.println("You don't have any added friends and groups");
            return;
        }
        if (!server.getFriendsList(username).isEmpty()) {
            writer.println("Friends:");
            getStatusForFriends(server.getFriendsList(username).entrySet(), writer);
        }
        if (!server.getGroupsOfUser(username).isEmpty()) {
            writer.println("Groups:");
            getStatusForGroups(server.getGroupsOfUser(username).entrySet(), writer);
>>>>>>> master
        }
    }

    private void getStatusForFriends(Set<Map.Entry<String, Friend>> allFriends, PrintWriter writer) {

        for (Map.Entry<String, Friend> friend : allFriends) {
            StringBuilder message = new StringBuilder();
            message.append(String.format("* %s (%s): ", server.getProfileNames(friend.getKey()), friend.getKey()));
            getStatusForOneClient(message, friend.getValue().getAmount());
            writer.println(message.toString());
        }
    }

    private void getStatusForGroups(Set<Map.Entry<String, Group>> allGroups, PrintWriter writer) {
        for (Map.Entry<String, Group> group : allGroups) {
            writer.println(String.format("* %s", group.getKey()));
<<<<<<< HEAD

            getStatusForFriends(server.getMembersInGroup(username, group.getKey()), writer);
=======
            getStatusForFriends(server.getGroupsOfUser(username).get(group.getKey()).getAllMembersInGroup(), writer);
>>>>>>> master
        }
    }

    private void payed(PrintWriter writer, String[] tokens) throws IOException {
        if (tokens.length == 3) {
            String friend = tokens[2];
            double amount = Double.parseDouble(tokens[1]);
<<<<<<< HEAD
            // double exchangeRate = getExchangeRate(server.getRate(friend));
//            server.getFriendsList(username).get(friend).decrease(amount * (1 / rate));
//            server.getFriendsList(friend).get(username).increase(amount * (1 / rate));

            server.decreaseAmountOfFriend(username, friend, amount);
            server.increaseAmountOfFriend(friend, username, amount);

            // double dueAmount = server.getFriendsList(username).get(friend).getAmount();
=======
            server.getFriendsList(username).get(friend).decrease(amount * (1 / rate));
            server.getFriendsList(friend).get(username).increase(amount * (1 / rate));
>>>>>>> master
            sendMessageAfterPayed(writer, amount, friend, false);
        } else {
            writer.println(ERROR_MESSAGE);
        }
    }

    private void payedGroup(PrintWriter writer, String[] tokens) throws IOException {
        if (tokens.length == 4) {
            double amount = Double.parseDouble(tokens[1]);
            String group = tokens[2];
            String friend = tokens[3];
<<<<<<< HEAD
            // double exchangeRate = getExchangeRate(server.getRate(friend));
//            server.getGroupsOfUser(username).get(group).decreaseAmountOfFriend(friend, amount * rate);
//            server.getGroupsOfUser(friend).get(group).increaseAmountOfFriend(username, amount * rate);
            server.decreaseAmountOfGroupMember(username, group, friend, amount);
            server.increaseAmountOfGroupMember(friend, group, username, amount);
=======
            server.getGroupsOfUser(username).get(group).decreaseAmountOfFriend(friend, amount * rate);
            server.getGroupsOfUser(friend).get(group).increaseAmountOfFriend(username, amount * rate);
>>>>>>> master
            sendMessageAfterPayed(writer, amount, friend, true);
        } else {
            writer.println(ERROR_MESSAGE);
        }
    }

    private void sendMessageAfterPayed(PrintWriter writer, double amount, String receiver, boolean isGroup) {

        StringBuilder message = new StringBuilder();
        message.append("Current status: ");
<<<<<<< HEAD
//         getStatusForOneClient(message, amount * rate);
        message.append(String.format("%nYou payed %s to %s", amount, receiver));
        writer.println(message.toString());

//        String messageForFriend = String.format("%s approved your payment %s %s", server.getProfileNames(username),
//                amount, currency);
        String messageForFriend = String.format("%s approved your payment %s %s", server.getProfileNames(username),
                amount);

=======
        message.append(String.format("%nYou payed %s to %s", amount, receiver));
        writer.println(message.toString());

        String messageForFriend = String.format("%s approved your payment %s %s", server.getProfileNames(username),
                amount, currency);
>>>>>>> master
        if (!isGroup) {
            sendFriendNotification(receiver, messageForFriend);
        } else {
            sendGroupNotification(receiver, messageForFriend);
        }
    }

    private void getStatusForOneClient(StringBuilder messageLine, double amount) {

        double result = amountAfterRoundUp(amount);
        if (amount > 0) {
<<<<<<< HEAD
//            messageLine.append(String.format("Owes you %s %s.", result, currency));
            messageLine.append(String.format("Owes you %s %s.", result));

        } else if (amount < 0) {
//            messageLine.append(String.format("You owe %s %s", MINUS * result, currency));
            messageLine.append(String.format("You owe %s %s", MINUS * result));

        } else {
            messageLine.append("Good accounts good friends");
        }

=======
            messageLine.append(String.format("Owes you %s %s.", result, currency));
        } else if (amount < 0) {
            messageLine.append(String.format("You owe %s %s", MINUS * result, currency));
        } else {
            messageLine.append("Good accounts good friends");
        }
>>>>>>> master
    }

    private double amountAfterRoundUp(double amount) {

        double scale = Math.pow(10, 2);
<<<<<<< HEAD
//        return Math.round(amount * rate * scale) / scale;
        return Math.round(amount * scale) / scale;

    }

    private void createGroup(PrintWriter writer, String[] tokens) throws IOException {

        if (tokens.length >= 4) {
            String nameOfGroup = tokens[1];
=======
        return Math.round(amount * rate * scale) / scale;
    }

    private void createGroup(PrintWriter writer, String[] tokens) throws IOException {
        if (tokens.length >= 4) {
            String nameOfTheGroup = tokens[1];
>>>>>>> master
            List<String> friends = new ArrayList<>();
            for (int i = 2; i < tokens.length; ++i) {
                friends.add(tokens[i]);
            }
            Group group = new Group(friends);
<<<<<<< HEAD
            server.addGroup(username, nameOfGroup, group);
            writer.printf(String.format("You created the group %s.%n", nameOfGroup));
=======
            server.getGroupsOfUser(username).put(nameOfTheGroup, group);

            writer.printf(String.format("You created the group %s.%n", nameOfTheGroup));
>>>>>>> master

            friends.add(username);
            for (int i = 2; i < tokens.length; ++i) {
                List<String> newStr = new ArrayList<>(friends);
                newStr.remove(tokens[i]);
<<<<<<< HEAD
                server.addGroup(tokens[i], nameOfGroup, new Group(newStr));
                String message = String.format("* %s:%n%s created group with you.", nameOfGroup,
=======
                server.getGroupsOfUser(tokens[i]).put(nameOfTheGroup, new Group(newStr));

                String message = String.format("* %s:%n%s created group with you.", nameOfTheGroup,
>>>>>>> master
                        server.getProfileNames(username));
                sendGroupNotification(tokens[i], message);
            }
        } else {
            writer.println(ERROR_MESSAGE);
        }
    }

    private boolean sendNotification(String receiver, String message) {

        if (server.isActive(receiver)) {
            Socket toUser = server.getSocket(receiver);
            PrintWriter toWriter;
            try {
                toWriter = new PrintWriter(toUser.getOutputStream(), true);
                toWriter.print(String.format("[%s] ", NOTIFICATION));
                toWriter.println(message);
                return true;
            } catch (IOException e) {
                System.out.println("Problem with application, try again later.");
                System.err.println("Exception thrown by Print Writer: " + e.getMessage());
            }
        }
        return false;
    }

    private void sendFriendNotification(String receiver, String message) {
        if (!sendNotification(receiver, message)) {
<<<<<<< HEAD
            server.sendFriendNotificationToNotActive(receiver, message);
=======
            server.addFriendNotification(receiver, message);
>>>>>>> master
        }
    }

    private void sendGroupNotification(String receiver, String message) {
        if (!sendNotification(receiver, message)) {
<<<<<<< HEAD
            server.sendGroupNotificationToNotActive(receiver, message);
        }
    }

//    private void switchCurrency(PrintWriter writer, String[] tokens) {
//        if (tokens.length == 2) {
//            if (currencyIsValid(tokens[1])) {
//                RateHandler rateHandler = new RateHandler(currency, tokens[1]);
//                System.out.println(rateHandler.getRate());
//                rate *= rateHandler.getRate();
//                server.setRate(username, rate);
//                writer.println(
//                        String.format("You successfully changed the currency from %s to %s.", currency, tokens[1]));
//                currency = tokens[1];
//            } else {
//                writer.println(String.format("%s is not valid currency.", tokens[1]));
//            }
//        } else {
//            writer.println(ERROR_MESSAGE);
//        }
//    }
=======
            server.addGroupNotification(receiver, message);
        }
    }

    private void switchCurrency(PrintWriter writer, String[] tokens) {
        if (tokens.length == 2) {
            if (currencyIsValid(tokens[1])) {
                RateHandler rateHandler = new RateHandler(currency, tokens[1]);
                System.out.println(rateHandler.getRate());
                rate *= rateHandler.getRate();
                server.setRate(username, rate);
                writer.println(
                        String.format("You successfully changed the currency from %s to %s.", currency, tokens[1]));
                currency = tokens[1];
            } else {
                writer.println(String.format("%s is not valid currency.", tokens[1]));
            }
        } else {
            writer.println(ERROR_MESSAGE);
        }
    }
>>>>>>> master

    private boolean currencyIsValid(String currency) {
        return currency.equals("USD") || currency.equals("BGN") || currency.equals("EUR");
    }

<<<<<<< HEAD
//	private double getExchangeRate(double friendRate) {
//		return friendRate / rate;
//	}
=======
>>>>>>> master

    private void writeInPaymentFile(String paymentMessage) {
        File file = server.getFile(username);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(paymentMessage);
        } catch (IOException e) {
            System.err.println("Exception thrown by fileWriter: " + e.getMessage());
        }
    }

    private void historyOfPayment(PrintWriter writer) {
        File file = server.getFile(username);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.println(line);
            }
        } catch (IOException e) {
            System.err.println("Exception thrown by readLine: " + e.getMessage());
        }
    }

}
