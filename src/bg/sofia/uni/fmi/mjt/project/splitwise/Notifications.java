package bg.sofia.uni.fmi.mjt.project.splitwise;

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

	public boolean isEmpty() {
		return friendsNotifications.size() + groupsNotifications.size() == 0;
	}

	public void addFrindNotification(String nofitication) {
		friendsNotifications.add(nofitication);
	}

	public void addGroupNotification(String notification) {
		groupsNotifications.add(notification);
	}

	public String getAllNotifications() {
		StringBuilder s = new StringBuilder();
		if (!friendsNotifications.isEmpty()) {
			s.append(FRIENDS_HEAD_MESSAGE);
			s.append(System.getProperty("line.separator"));
			for (String notification : friendsNotifications) {
				s.append(notification);
			}
		}

		if (!groupsNotifications.isEmpty()) {
			s.append(GROUPS_HEAD_MESSAGE);
			s.append(System.getProperty("line.separator"));
			for (String notification : groupsNotifications) {
				s.append(notification);
			}
		}

		return s.toString();
	}

}
