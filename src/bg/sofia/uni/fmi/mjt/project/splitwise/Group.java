package bg.sofia.uni.fmi.mjt.project.splitwise;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Group {
	private Map<String, Friend> groupFriends;

	public Group(String[] friends) {
		groupFriends = new HashMap<>();
		for (String friend : friends) {
			groupFriends.put(friend, new Friend());
		}

	}

	public Group(List<String> friends) {
		groupFriends = new HashMap<>();
		for (String friend : friends) {
			groupFriends.put(friend, new Friend());
		}

	}

	public double getFriendAmount(String friend) {
		return groupFriends.get(friend).getAmount();
	}

	public void splitMoney(double amount) {
		for (String friend : groupFriends.keySet()) {
			increaseAmountOfFriend(friend, amount);
		}
	}

	public void increaseAmountOfFriend(String friend, double amount) {
		groupFriends.get(friend).increase(amount);
	}

	public void decreaseAmountOfFriend(String friend, double amount) {
		groupFriends.get(friend).decrease(amount);
	}

	public int getNumberOfMembers() {
		return groupFriends.size() + 1;
	}

	public Set<Map.Entry<String, Friend>> getAllMembersInGroup() {
		return groupFriends.entrySet();
	}

	public Set<String> getAllNamesOfMembers() {
		return groupFriends.keySet();
	}

}
