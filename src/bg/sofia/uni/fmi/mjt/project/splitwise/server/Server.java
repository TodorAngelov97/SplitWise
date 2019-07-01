package bg.sofia.uni.fmi.mjt.project.splitwise.server;

import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Friend;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Group;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.UserProfile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    public static final int PORT = 8080;
    private static final String FILE_NAME = "resources/users.json";
    private ServerSocket socketOfServer;

    private Map<String, Socket> activeUsers;
    private Set<UserProfile> profiles;
    private Map<String, UserData> usersData;
    private String fileName;

    public Server(ServerSocket socketOfServer, String fileName) {
        this.fileName = fileName;
        initializeConstructor(socketOfServer);
    }

    private void initializeConstructor(ServerSocket socketOfServer) {
        this.socketOfServer = socketOfServer;
        activeUsers = new ConcurrentHashMap<>();
        profiles = new HashSet<>();
        usersData = new ConcurrentHashMap<>();
        loadUsers();
    }


    private void loadUsers() {
        Set<UserProfile> users = getUsers();
        for (UserProfile user : users) {
            addUser(user.getUsername(), user);
        }
    }

    private Set<UserProfile> getUsers() {
        Set<UserProfile> users = null;
        try {
            File file = new File(fileName);
            if (!isFileEmpty(file)) {

                Gson gson = new Gson();
                Type type = new TypeToken<HashSet<UserProfile>>() {
                }.getType();
                JsonReader readerForUsers = new JsonReader(new FileReader(file));
                users = gson.fromJson(readerForUsers, type);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Exception thrown by Json: " + e.getMessage());
        }
        return users;
    }

    private boolean isFileEmpty(File file) {
        return file.length() == 0;
    }

    public void addUser(String username, UserProfile newUserProfile) {
        profiles.add(newUserProfile);
        UserData userData = new UserData(newUserProfile);
        usersData.put(username, userData);
    }

    public boolean isUsernameContained(String user) {
        return usersData.containsKey(user);
    }

    public int getNumberOfUsers() {
        return profiles.size();
    }

    public int getNumberOfActiveUsers() {
        return activeUsers.size();
    }

    public Socket getSocket(String username) {
        return activeUsers.get(username);
    }

    public void removeUser(String username) {
        activeUsers.remove(username);
    }

    public synchronized void saveUserInFile() {

        try (Writer writer = new FileWriter(fileName)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(profiles, writer);
        } catch (IOException e) {
            System.err.println("Exception thrown by Json: " + e.getMessage());
        }
    }

    public boolean isLoggedIn(String name, String password, PrintWriter writer) {

        if (!isUsernameContained(name)) {
            final String MESSAGE = "Non-existent user";
            writer.println(MESSAGE);
            return false;
        } else if (!isCorrectPassword(name, password)) {
            final String MESSAGE = "Incorrect password";
            writer.println(MESSAGE);
            return false;
        }
        return true;
    }

    private boolean isCorrectPassword(String user, String password) {
        UserData userData = usersData.get(user);
        return userData.isPasswordCorrect(password);
    }

    public void printUserNotifications(String username, PrintWriter writer) {
        UserData userData = usersData.get(username);
        userData.printUserNotifications(writer);
    }

    public boolean isUserInFriends(String username, String friend) {
        UserData userData = usersData.get(username);
        return userData.isUserInFriends(friend);
    }

    public void addFriend(String username, String friend) {
        UserData userData = usersData.get(username);
        userData.addFriend(friend);
    }

    public String getProfileNames(String username) {
        UserData userData = usersData.get(username);
        return userData.getProfileNames();
    }

    public File getFile(String username) {
        UserData userData = usersData.get(username);
        return userData.getFile();
    }

    public void addGroup(String username, String name, Group group) {
        UserData userData = usersData.get(username);
        userData.addGroup(name, group);
    }

    public boolean hasNotFriends(String username) {
        UserData userData = usersData.get(username);
        return userData.hasNotFriends();
    }

    public boolean hasNotGroups(String username) {
        UserData userData = usersData.get(username);
        return userData.hasNotGroups();
    }

    public void increaseAmountOfFriend(String username, String friend, double amount) {
        UserData userData = usersData.get(username);
        userData.increaseAmountOfFriend(friend, amount);
    }

    public void decreaseAmountOfFriend(String username, String friend, double amount) {
        UserData userData = usersData.get(username);
        userData.decreaseAmountOfFriend(friend, amount);
    }

    public double getFriendAmount(String username, String friend) {
        UserData userData = usersData.get(username);
        return userData.getFriendAmount(friend);
    }

    public int getNumberOfMembersInGroup(String username, String groupName) {
        UserData userData = usersData.get(username);
        return userData.getNumberOfMembersInGroup(groupName);
    }

    public Set<String> getMembersNamesInGroup(String username, String groupName) {
        UserData userData = usersData.get(username);
        return userData.getMembersNamesInGroup(groupName);
    }

    public void decreaseAmountOfGroupMember(String username, String groupName, String friend, double amount) {
        UserData userData = usersData.get(username);
        userData.decreaseAmountOfGroupMember(groupName, friend, amount);
    }

    public void increaseAmountOfGroupMember(String username, String groupName, String friend, double amount) {
        UserData userData = usersData.get(username);
        userData.increaseAmountOfGroupMember(groupName, friend, amount);
    }

    public void increaseAmountOfGroup(String username, String groupName, double amount) {
        Group group = getGroups(username).get(username);
        group.splitMoney(amount);
    }

    public Map<String, Friend> getFriends(String username) {
        UserData userData = usersData.get(username);
        return userData.getFriends();
    }

    public Map<String, Group> getGroups(String username) {
        UserData userData = usersData.get(username);
        return userData.getGroups();
    }

    public Set<Map.Entry<String, Friend>> getMembersInGroup(String username, String groupName) {
        UserData userData = usersData.get(username);
        return userData.getMembersInGroup(groupName);
    }

    public void addNewActiveUser(String username, Socket socket) {
        activeUsers.put(username, socket);
    }

    public int getNumberOfFriends(String username) {
        UserData userData = usersData.get(username);
        return userData.getNumberOfFriends();
    }

    public void sendFriendNotification(String receiver, String message) {
        if (isActive(receiver)) {
            sendNotificationToActive(receiver, message);
        } else {
            sendNotificationToNotActiveFriend(receiver, message);
        }
    }

    private boolean isActive(String username) {
        return activeUsers.containsKey(username);
    }

    private void sendNotificationToActive(String receiver, String message) {
        Socket toUser = getSocket(receiver);
        PrintWriter toWriter;
        try {
            toWriter = new PrintWriter(toUser.getOutputStream(), true);
            final String NOTIFICATION = "*Notification*";
            toWriter.print(String.format("[%s] ", NOTIFICATION));
            toWriter.println(message);
        } catch (IOException e) {
            System.err.println("Exception thrown by Print Writer: " + e.getMessage());
        }
    }


    private void sendNotificationToNotActiveFriend(String receiver, String message) {
        UserData userData = usersData.get(receiver);
        userData.addFriendNotification(message);
    }

    public void sendGroupNotification(String receiver, String message) {
        if (isActive(receiver)) {
            sendNotificationToActive(receiver, message);
        } else {
            sendGroupNotificationToNotActive(receiver, message);
        }
    }

    private void sendGroupNotificationToNotActive(String receiver, String message) {
        UserData userData = usersData.get(receiver);
        userData.addGroupNotification(message);
    }

    public void writeInPaymentFile(String paymentMessage, String username) {
        File file = getFile(username);
        try (FileWriter fileWriter = new FileWriter(file, true);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {
            writer.write(paymentMessage);
        } catch (IOException e) {
            System.err.println("Exception thrown by fileWriter: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            Server server = new Server(serverSocket, FILE_NAME);
            server.execute();
        } catch (IOException e) {
            System.err.println("Exception thrown by SeverSocket: " + e.getMessage());
        }
    }

    public void execute() {
        final String WELCOME_MESSAGE = String.format("ServerOld is running on localhost:%d%n", PORT);
        System.out.println(WELCOME_MESSAGE);
        try {
            while (true) {
                Socket socket = socketOfServer.accept();
                startNewThreadForUser(socket);
            }
        } catch (IOException e) {
            System.err.println("Exception thrown by Socket: " + e.getMessage());
        } finally {
            try {
                socketOfServer.close();
            } catch (IOException e) {
                System.err.println("Exception thrown by close Socket: " + e.getMessage());
            }
        }
    }

    private void startNewThreadForUser(Socket socket) {
        ClientConnection runnable = new ClientConnection(socket, this);
        Thread newClient = new Thread(runnable);
        newClient.start();
    }

}
