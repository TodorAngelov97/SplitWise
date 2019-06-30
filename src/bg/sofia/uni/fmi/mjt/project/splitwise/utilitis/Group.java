package bg.sofia.uni.fmi.mjt.project.splitwise.utilitis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Group {
    private Map<String, Friend> groupFriends;

    public Group(List<String> friends) {
        groupFriends = new HashMap<>();
        for (String friend : friends) {
            groupFriends.put(friend, new Friend());
        }

    }

    public double getFriendAmount(String friendsName) {
        Friend friend = groupFriends.get(friendsName);
        return friend.getAmount();
    }

    public void splitMoney(double amount) {
        for (String friend : groupFriends.keySet()) {
            increaseAmountOfFriend(friend, amount);
        }
    }

    public void increaseAmountOfFriend(String friendsName, double amount) {
        Friend friend = groupFriends.get(friendsName);
        friend.increase(amount);
    }

    public void decreaseAmountOfFriend(String friendsName, double amount) {
        Friend friend = groupFriends.get(friendsName);
        friend.decrease(amount);
    }

    public int getNumberOfMembers() {
        return groupFriends.size() + 1;
    }

    public Set<Map.Entry<String, Friend>> getMembersInGroup() {
        return groupFriends.entrySet();
    }

    public Set<String> getAllNamesOfMembers() {
        return groupFriends.keySet();
    }

}
