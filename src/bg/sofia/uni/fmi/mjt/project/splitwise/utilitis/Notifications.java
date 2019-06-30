package bg.sofia.uni.fmi.mjt.project.splitwise.utilitis;

import java.util.ArrayList;
import java.util.List;

public class Notifications {
    private List<String> friendsNotifications;
    private List<String> groupsNotifications;

    private static final String FRIENDS_HEAD_MESSAGE = "Friends: ";
    private static final String GROUPS_HEAD_MESSAGE = "Groups: ";

    public Notifications() {
        this.friendsNotifications = new ArrayList<>();
        this.groupsNotifications = new ArrayList<>();
    }

    public boolean isNotEmpty() {
        return friendsNotifications.size() + groupsNotifications.size() != 0;
    }

    public void addFriendNotification(String notification) {
        friendsNotifications.add(notification);
    }

    public void addGroupNotification(String notification) {
        groupsNotifications.add(notification);
    }

    public String getAllNotifications() {
        StringBuilder message = new StringBuilder();
        appendFriendsNotifications(message);
        appendGroupsNotifications(message);
        return message.toString();
    }

    private void appendFriendsNotifications(StringBuilder message) {
        if (!friendsNotifications.isEmpty()) {
            message.append(FRIENDS_HEAD_MESSAGE);
            message.append(System.getProperty("line.separator"));
            for (String notification : friendsNotifications) {
                message.append(notification);
            }
        }
    }


    private void appendGroupsNotifications(StringBuilder message) {
        if (!groupsNotifications.isEmpty()) {
            message.append(GROUPS_HEAD_MESSAGE);
            message.append(System.getProperty("line.separator"));
            for (String notification : groupsNotifications) {
                message.append(notification);
            }
        }
    }
}

