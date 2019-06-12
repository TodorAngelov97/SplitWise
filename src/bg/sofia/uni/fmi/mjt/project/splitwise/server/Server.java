package bg.sofia.uni.fmi.mjt.project.splitwise.server;

import bg.sofia.uni.fmi.mjt.project.splitwise.Friend;
import bg.sofia.uni.fmi.mjt.project.splitwise.Group;
import bg.sofia.uni.fmi.mjt.project.splitwise.UserProfile;
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
    static final double INITIAL_RATE = 1;
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
        profiles = new HashSet<>();//////>>>>>>>>>>>>
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

    //magic
    public synchronized void addUser(String username, UserProfile newUserProfile) {
        profiles.add(newUserProfile);
        usersData.put(username, new UserData(newUserProfile));
//        ratesOfCurrencies.put(username, INITIAL_RATE);
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

    public boolean isActive(String username) {
        return activeUsers.containsKey(username);
    }

    public Socket getSocket(String username) {
        return activeUsers.get(username);
    }

    public void removeUser(String username) {
        activeUsers.remove(username);
    }

    //not sure to be synchronize
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
            writer.println("Non-existent user");
            return false;
        } else if (!isCorrectPassword(name, password)) {
            writer.println("Incorrect password");
            return false;
        }
        return true;
    }

    public boolean isCorrectPassword(String user, String password) {
        return usersData.get(user).isPasswordCorrect(password);

    }

    public void printUserNotifications(String username, PrintWriter writer) {
        usersData.get(username).printUserNotifications(writer);
    }

    public boolean isUserInFriends(String username, String friend) {
        return usersData.get(username).isUserInFriends(friend);
    }

    public void addFriend(String username, String friend) {
        usersData.get(username).addFriend(friend);
    }

    public String getProfileNames(String username) {
        return usersData.get(username).getProfileNames();
    }

    public File getFile(String username) {
        return usersData.get(username).getFile();
    }

    public void sendFriendNotificationToNotActive(String receiver, String message) {
        usersData.get(receiver).addFriendNotification(message);
    }

    public void sendGroupNotificationToNotActive(String receiver, String message) {
        usersData.get(receiver).addGroupNotification(message);
    }

    public void addGroup(String username, String name, Group group) {
        usersData.get(username).addGroup(name, group);
    }

    public boolean hasNotFriends(String username) {
        return usersData.get(username).hasNotFriends();
    }

    public boolean hasNotGroups(String groupName) {
        return usersData.get(groupName).hasNotGroups();
    }

    public void increaseAmountOfFriend(String username, String friend, double amount) {
        usersData.get(username).increaseAmountOfFriend(friend, amount);
    }

    public void decreaseAmountOfFriend(String username, String friend, double amount) {
        usersData.get(username).decreaseAmountOfFriend(friend, amount);
    }

    public double getFriendAmount(String username, String friend) {
        return usersData.get(username).getFriendAmount(friend);
    }

    public int getNumberOfMembersInGroup(String username, String groupName) {
        return usersData.get(username).getNumberOfMembersInGroup(groupName);
    }

    public Set<String> getMembersNamesInGroup(String username, String groupName) {
        return usersData.get(username).getMembersNamesInGroup(groupName);
    }

    public void decreaseAmountOfGroupMember(String username, String groupName, String friend, double amount) {
        usersData.get(username).decreaseAmountOfGroupMember(groupName, friend, amount);
    }

    public void increaseAmountOfGroupMember(String username, String groupName, String friend, double amount) {
        usersData.get(username).increaseAmountOfGroupMember(groupName, friend, amount);
    }

    public Map<String, Friend> getFriends(String username) {
        return usersData.get(username).getFriends();
    }

    public Map<String, Group> getGroups(String username) {
        return usersData.get(username).getGroups();
    }

    public Set<Map.Entry<String, Friend>> getMembersInGroup(String username, String groupName) {
        return usersData.get(username).getMembersInGroup(groupName);
    }

    public void sendFriendNotification(String receiver, String message) {
        if (!sendNotification(receiver, message)) {
            sendFriendNotificationToNotActive(receiver, message);
        }
    }

    private boolean sendNotification(String receiver, String message) {

        if (isActive(receiver)) {
            Socket toUser = getSocket(receiver);
            PrintWriter toWriter;
            try {
                final String NOTIFICATION = "*Notification*";
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


    public void execute() {
        System.out.printf("ServerOld is running on localhost:%d%n", PORT);
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

    public void addNewActiveUser(String username, Socket socket) {
        activeUsers.put(username, socket);
    }

    //

    private void startNewThreadForUser(Socket socket) {
        ClientConnection runnable = new ClientConnection(socket, this);
        new Thread(runnable).start();
    }

    public int getNumberOfFriends(String username) {
        return usersData.get(username).getFriends().size();
    }

    public void sendNofiticationToFriend(String receiver, String message) {
        if (isActive(receiver)) {
            sendNotificationToActive(receiver, message);
        } else {
            sendFriendNotificationToNotActive(receiver, message);
        }
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
            System.out.println("Problem with application, try again later.");
            System.err.println("Exception thrown by Print Writer: " + e.getMessage());
        }
    }


    public void sendGroupNotification(String receiver, String message) {
        if (isActive(receiver)) {
            sendNotificationToActive(receiver, message);
        } else {
            sendGroupNotificationToNotActive(receiver, message);
        }
    }

    public static void main(String[] args) {
        try {
            ServerOld server = new ServerOld(new ServerSocket(PORT), FILE_NAME);
            server.execute();
        } catch (IOException e) {
            System.err.println("Exception thrown by SeverSocket: " + e.getMessage());
        }
    }
}
