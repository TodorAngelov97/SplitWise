package bg.sofia.uni.fmi.mjt.project.splitwise.server;

import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Friend;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Group;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.Notifications;
import bg.sofia.uni.fmi.mjt.project.splitwise.utilitis.UserProfile;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserData {
    private static final String PATH_TO_PAYMENT_DIR = "resources/payment/";

    private UserProfile userProfile;
    private Map<String, Friend> friends;
    private Map<String, Group> groups;
    private Notifications notifications;
    private File fileWithHistoryOfPayments;


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

    public int getNumberOfFriends() {
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

    public void printUserNotifications(PrintWriter writer) {
        if (hasNotifications()) {
            writer.println(getUserNotifications());
        } else {
            final String MESSAGE = "No notifications to show";
            writer.println(MESSAGE);
        }
    }

    private boolean hasNotifications() {
        return notifications.isNotEmpty();
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

    public void increaseAmountOfFriend(String friendsName, double amount) {
        Friend friend = friends.get(friendsName);
        friend.increase(amount);
    }

    public void decreaseAmountOfFriend(String friendsName, double amount) {
        Friend friend = friends.get(friendsName);
        friend.decrease(amount);
    }

    public double getFriendAmount(String friendsName) {
        Friend friend = friends.get(friendsName);
        return friend.getAmount();
    }

    public Set<String> getMembersNamesInGroup(String groupName) {
        Group group = groups.get(groupName);
        return group.getAllNamesOfMembers();
    }

    public int getNumberOfMembersInGroup(String groupName) {
        Group group = groups.get(groupName);
        return group.getNumberOfMembers();
    }

    public void decreaseAmountOfGroupMember(String groupName, String friend, double amount) {
        Group group = groups.get(groupName);
        group.decreaseAmountOfFriend(friend, amount);
    }

    public void increaseAmountOfGroupMember(String groupName, String friend, double amount) {
        Group group = groups.get(groupName);
        group.increaseAmountOfFriend(friend, amount);
    }

    public Set<Map.Entry<String, Friend>> getMembersInGroup(String groupName) {
        Group group = groups.get(groupName);
        return group.getMembersInGroup();
    }

    public boolean isPasswordCorrect(String password) {
        return userProfile.isPasswordCorrect(password);
    }

    public void addFriend(String friend) {
        friends.put(friend, new Friend());
    }
}
