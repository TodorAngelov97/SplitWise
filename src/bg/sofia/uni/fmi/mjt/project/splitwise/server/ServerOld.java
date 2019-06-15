package bg.sofia.uni.fmi.mjt.project.splitwise.server;

import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Friend;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Group;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Notifications;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.UserProfile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ServerOld {

    public static final int PORT = 8080;
    static final double INITIAL_RATE = 1;
    private static final String FILE_NAME = "resources/users.json";
    private static final String PATH_TO_PAYMENT_DIR = "resources/payment/";
    private ServerSocket socketOfServer;
    private Map<String, Socket> activeUsers;
    private Set<UserProfile> justProfiles;
    private Map<String, UserProfile> allProfiles;
    private Map<String, Map<String, Friend>> friendsList;
    private Map<String, Map<String, Group>> groups;
    private Map<String, Notifications> usersNotifications;
    private Map<String, File> filesWithHistoryOfPay;
    private Map<String, Double> ratesOfCurrencies;//not used yet -->implements switch currency
    private String fileName;

    public ServerOld(ServerSocket socketOfServer, String fileName) {
        this.fileName = fileName;
        initializeConstructor(socketOfServer);

    }

    public int getNumberOfFriends(String username) {
        return friendsList.get(username).size();
    }

    public int getNumberOfUsers() {
        return justProfiles.size();
    }

    public int getNumberOfActiveUsers() {
        return activeUsers.size();
    }

    public synchronized double getRate(String username) {
        return ratesOfCurrencies.get(username);
    }

    public synchronized void setRate(String username, double rate) {
        ratesOfCurrencies.put(username, rate);
    }

    private void initializeConstructor(ServerSocket socketOfServer) {
        this.socketOfServer = socketOfServer;
        activeUsers = new HashMap<>();
        justProfiles = new HashSet<>();
        allProfiles = new HashMap<>();
        friendsList = new HashMap<>();
        groups = new HashMap<>();
        filesWithHistoryOfPay = new HashMap<>();
        usersNotifications = new HashMap<>();
        ratesOfCurrencies = new HashMap<>();
        loadUser();

    }

    public synchronized File getFile(String username) {
        return filesWithHistoryOfPay.get(username);
    }

    public synchronized void addFile(String username) {
        String filePath = PATH_TO_PAYMENT_DIR + username;
        File newFile = new File(filePath);
        try {
            newFile.createNewFile();
            filesWithHistoryOfPay.put(username, newFile);
        } catch (IOException e) {
            System.err.println("Exception thrown by newFile: " + e.getMessage());
        }
    }

    public synchronized void addFriendNotification(String username, String notification) {
        usersNotifications.get(username).addFriendNotification(notification);
    }

    public synchronized void addGroupNotification(String username, String notification) {
        usersNotifications.get(username).addGroupNotification(notification);
    }

    private synchronized String getUserNotifications(String username) {
        return usersNotifications.get(username).getAllNotifications();
    }

    private synchronized boolean hasNotifications(String username) {
        return !(usersNotifications.get(username).isNotEmpty());
    }

    public synchronized void printUserNotifications(PrintWriter writer, String username) {
        if (hasNotifications(username)) {
            writer.println(getUserNotifications(username));
        } else {
            String message = "No notifications to show";
            writer.println(message);
        }
    }

    public synchronized boolean isActive(String username) {
        return activeUsers.containsKey(username);
    }

    public synchronized Socket getSocket(String username) {
        return activeUsers.get(username);
    }

    public synchronized boolean isUsernameContained(String user) {
        return allProfiles.containsKey(user);

    }

    public synchronized boolean isGroupNameContained(String user) {
        return allProfiles.containsKey(user);

    }

    public synchronized boolean isCorrectPassword(String user, String password) {
        return allProfiles.get(user).getPassword().equals(password);
    }

    public synchronized String getProfileNames(String username) {
        return allProfiles.get(username).getProfileNames();
    }

    public synchronized Map<String, Friend> getFriendsList(String username) {
        return friendsList.get(username);
    }

    public synchronized void removeUser(String username) {
        activeUsers.remove(username);
    }

    public synchronized Map<String, Group> getGroupsOfUser(String username) {
        return groups.get(username);
    }

    public synchronized boolean isLoggedIn(String name, String password, PrintWriter writer) {

        if (!isUsernameContained(name)) {
            writer.println("Non-existent user");
            return false;
        } else if (!isCorrectPassword(name, password)) {
            writer.println("Incorrect password");
            return false;
        }
        return true;
    }

    private boolean isFileEmpty(File file) {
        return file.length() == 0;
    }

    private void addUsers(Set<UserProfile> users) {
        for (UserProfile user : users) {
            addNewUser(user.getUsername(), user);
        }
    }

    public synchronized void addNewUser(String username, UserProfile newUserProfile) {
        justProfiles.add(newUserProfile);
        allProfiles.put(username, newUserProfile);
        friendsList.put(username, new HashMap<String, Friend>());
        groups.put(username, new HashMap<String, Group>());
        usersNotifications.put(username, new Notifications());
        ratesOfCurrencies.put(username, INITIAL_RATE);
        addFile(username);
    }

    private void loadUser() {

        try {
            File file = new File(fileName);
            if (!isFileEmpty(file)) {

                Gson gson = new Gson();
                Type type = new TypeToken<HashSet<UserProfile>>() {
                }.getType();
                JsonReader readerForUsers = new JsonReader(new FileReader(file));
                Set<UserProfile> data = gson.fromJson(readerForUsers, type);
                addUsers(data);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Exception thrown by Json: " + e.getMessage());
        }
    }

    public synchronized void saveUserInFile() {

        try (Writer writer = new FileWriter(fileName)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(justProfiles, writer);
        } catch (IOException e) {
            System.err.println("Exception thrown by Json: " + e.getMessage());
        }
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

    public synchronized void addNewActiveUser(String username, Socket socket) {
        activeUsers.put(username, socket);
    }

    private void startNewThreadForUser(Socket socket) {
//        ClientConnection runnable = new ClientConnection(socket, this);
//        new Thread(runnable).start();
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
