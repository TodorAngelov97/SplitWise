package bg.sofia.uni.fmi.mjt.project.splitwise.server;

import bg.sofia.uni.fmi.mjt.project.splitwise.Friend;
import bg.sofia.uni.fmi.mjt.project.splitwise.Group;
import bg.sofia.uni.fmi.mjt.project.splitwise.Notifications;
import bg.sofia.uni.fmi.mjt.project.splitwise.UserProfile;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserData {
    private static final String PATH_TO_PAYMENT_DIR = "resources/payment/";
    static final double INITIAL_RATE = 1;

    private UserProfile userProfile;
    private Map<String, Friend> friends;
    private Map<String, Group> groups;
    private Notifications notifications;
    private File fileWithHistoryOfPayments;
    private Map<String, Double> ratesOfCurrencies;//not used yet -->implements switch currency


    public UserData(UserProfile userProfile) {
        initializeConstructor(userProfile);
        addFile();
    }

    private void initializeConstructor(UserProfile userProfile) {
        this.userProfile = userProfile;
        this.friends = new HashMap<>();
        this.groups = new HashMap<>();
        this.notifications = new Notifications();
    }

    public void addFile() {
        String username = userProfile.getUsername();
        String filePath = PATH_TO_PAYMENT_DIR + username;
        File newFile = new File(filePath);
        try {
            newFile.createNewFile();
            fileWithHistoryOfPayments = newFile;
        } catch (IOException e) {
            System.err.println("Exception thrown by newFile: " + e.getMessage());
        }
    }

    public int getNumberOfFriends(String username) {
        return friends.size();
    }

    public File getFile() {
        return fileWithHistoryOfPayments;
    }

    public void addFriendNotification(String notification) {
        notifications.addFriendNotification(notification);
    }

    public void addGroupNotification(String notification) {
        notifications.addGroupNotification(notification);
    }

    private String getUserNotifications() {
        return notifications.getAllNotifications();
    }

    private boolean hasNotifications() {
        return notifications.isNotEmpty();
    }

    public void printUserNotifications(PrintWriter writer) {
        if (hasNotifications()) {
            writer.println(getUserNotifications());
        } else {
            String message = "No notifications to show";
            writer.println(message);
        }
    }


    public String getProfileNames() {
        return userProfile.getProfileNames();
    }

    public Map<String, Friend> getFriends() {
        return friends;
    }

    public boolean isUserInFriends(String friend) {
        return friends.containsKey(friend);
    }

    public Map<String, Group> getGroups() {
        return groups;
    }

    public void addGroup(String name, Group group) {
        groups.put(name, group);
    }

    public boolean hasNotFriends() {
        return friends.size() == 0;
    }

    public boolean hasNotGroups() {
        return friends.size() == 0;
    }

    public void increaseAmountOfFriend(String friend, double amount) {
        friends.get(friend).increase(amount);
    }

    public void decreaseAmountOfFriend(String friend, double amount) {
        friends.get(friend).decrease(amount);
    }

    public double getFriendAmount(String friend) {
        return friends.get(friend).getAmount();
    }

    public Set<String> getMembersNamesInGroup(String groupName) {
        return groups.get(groupName).getAllNamesOfMembers();
    }

    public int getNumberOfMembersInGroup(String groupName) {
        return groups.get(groupName).getNumberOfMembers();
    }

    public void decreaseAmountOfGroupMember(String groupName, String friend, double amount) {
        groups.get(groupName).decreaseAmountOfFriend(friend, amount);
    }

    public void increaseAmountOfGroupMember(String groupName, String friend, double amount) {
        groups.get(groupName).increaseAmountOfFriend(friend, amount);
    }

    public Set<Map.Entry<String, Friend>> getMembersInGroup(String groupName) {
        return groups.get(groupName).getMembersInGroup();
    }

    public boolean isPasswordCorrect(String password) {
        return userProfile.isPasswordCorrect(password);
    }

    public double getRate(String username) {
        return ratesOfCurrencies.get(username);
    }

    public void setRate(String username, double rate) {
        ratesOfCurrencies.put(username, rate);
    }

    public void addFriend(String friend) {
        friends.put(friend, new Friend());
    }
}
