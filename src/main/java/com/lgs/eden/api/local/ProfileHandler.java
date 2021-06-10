package com.lgs.eden.api.local;

import com.lgs.eden.api.profile.friends.FriendConversationView;
import com.lgs.eden.api.profile.friends.FriendData;
import com.lgs.eden.api.profile.ProfileAPI;
import com.lgs.eden.api.profile.ProfileData;
import com.lgs.eden.api.profile.RecentGameData;
import com.lgs.eden.api.profile.friends.FriendShipStatus;
import com.lgs.eden.api.profile.friends.conversation.ConversationData;
import com.lgs.eden.api.profile.friends.messages.MessageData;
import com.lgs.eden.api.profile.friends.messages.MessageType;
import com.lgs.eden.utils.Utility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Implementation of ProfileAPI
 */
class ProfileHandler implements ProfileAPI {

    private final HashMap<Integer, ArrayList<FriendData>> friendList = new HashMap<>();

    @Override
    public ArrayList<FriendData> getFriendList(int userID, int currentUserID) {
        if (friendList.containsKey(userID)) return friendList.get(userID);

        ArrayList<FriendData> friends = new ArrayList<>();
        if (userID == 23){
            friends.add(getFriendData(24));
            friends.add(getFriendData(25));
            friends.add(getFriendData(26));
            friends.add(getFriendData(27));
        } else if ( userID != 25 ){
            friends.add(getFriendData(23));
        } else {
            friends.add(getFriendData(23));
            friends.add(getFriendData(24));
        }

        friendList.put(userID, friends);
        return friendList.get(userID);
    }

    @Override
    public ProfileData getProfileData(int userID, int currentUserID) {
        ObservableList<FriendData> friendDataObservableList = FXCollections.observableArrayList();
        for (FriendData f:this.getFriendList(userID, currentUserID)) {
            // only show friend with the profile shown
            if(!evaluateRelationShip(userID, f.id).equals(FriendShipStatus.FRIENDS)) continue;
            friendDataObservableList.add(
                    new FriendData(
                      f.getAvatarPath(),
                      f.name,
                      f.online,
                      f.id,
                      evaluateRelationShip(currentUserID, f.id)
                    )
            );
        }

        int friendNumber = friendDataObservableList.size(); // observable friend list may contains
        // less user that friendNumber so that not the real value

        RecentGameData[] recentGamesData = new RecentGameData[]{};

        if (userID == 23){
            recentGamesData = new RecentGameData[]{
                    new RecentGameData(Utility.loadImage("/games/prim-icon.png"), "Prim", 0, RecentGameData.PLAYING),
                    new RecentGameData(Utility.loadImage("/games/enigma-icon.png"), "Enigma", 1020, 30)
            };

            return new ProfileData("Raphik",23, "/avatars/23.png",
                    friendNumber, 9999,
                    "Raphiki is a great programmer at ENSIIE engineering school.",
                    new Date(), Date.from(Instant.parse("2020-12-03T10:15:30.00Z")), friendDataObservableList,
                    recentGamesData,
                    evaluateRelationShip(userID, currentUserID)
            );
        } else if (userID == 24){
            return new ProfileData("Raphik2",24, "/avatars/24.png", friendNumber, 0,
                    "No description yet.",
                    new Date(), Date.from(Instant.parse("2021-03-18T10:15:30.00Z")), friendDataObservableList,
                    recentGamesData,
                    evaluateRelationShip(userID, currentUserID)
            );
        } else if (userID == 25){
            return new ProfileData("Calistral",25, "/avatars/25.png", friendNumber, -1,
                    "No description yet.",
                    new Date(), Date.from(Instant.parse("2020-12-03T10:15:30.00Z")), friendDataObservableList,
                    recentGamesData,
                    evaluateRelationShip(userID, currentUserID)
            );
        } else if (userID == 26){
            return new ProfileData("Caliki", 26, "/avatars/26.png", friendNumber, 0,
                    "This is a really"+"This is a really"+"This is a really"+"This is a really"+"This is a really"
                            +"This is a really"+"This is a really"+"This is a really"+"This is a really"+"This is a really"
                            +"This is a really"+"This is a really"+"This is a really"+"This is a really"+"This is a really"
                            +"This is a really"+"This is a really"+"This is a really"+"This is a really"+"This is a really"
                            +"This is a really"+"This is a really"+"This is a really"+"This is a really"+"This is a really"
                            +"This is a really"+"This is a really"+"This is a really"+"This is a really"+"This is a really",
                    new Date(), Date.from(Instant.parse("2020-12-03T10:15:30.00Z")), friendDataObservableList,
                    recentGamesData,
                    evaluateRelationShip(userID, currentUserID)
            );
        } else if (userID == 27){
            return new ProfileData("Raphistro",27, "/avatars/27.png", friendNumber, 17570,
                    "No description yet.",
                    new Date(), Date.from(Instant.parse("2020-03-09T10:15:30.00Z")), friendDataObservableList,
                    recentGamesData,
                    evaluateRelationShip(userID, currentUserID)
            );
        }

        throw new IllegalStateException("Not supported");
    }

    @Override
    public void addFriend(int friendID, int currentUserID){
        ArrayList<FriendData> friendList = getFriendList(currentUserID, friendID);
        friendList.add(getFriendData(friendID));
    }

    @Override
    public void removeFriend(int friendID, int currentUserID){
        ArrayList<FriendData> friendList = getFriendList(friendID, currentUserID);
        friendList.remove(getFriendData(currentUserID));

        friendList = getFriendList(currentUserID, friendID);
        friendList.remove(getFriendData(friendID));
    }

    @Override
    public void acceptFriend(int friendID, int currentUserID) {
        ArrayList<FriendData> friendList = getFriendList(currentUserID, friendID);
        friendList.add(getFriendData(friendID));
    }

    @Override
    public void refuseFriend(int friendID, int currentUserID) {
        removeFriend(friendID, currentUserID);
    }

    // ------------------------------ Messages ----------------------------- \\

    @Override
    public FriendConversationView getMessageWithFriend(int friendID, int currentUserID) {
        ObservableList<MessageData> messages = FXCollections.observableArrayList();
        ObservableList<ConversationData> conversations = FXCollections.observableArrayList();

        // "we are faking the pick of the last recent one conv"
        if (friendID == -1) friendID = 24;

        messages.addAll(getUserMessagesWith(friendID, currentUserID));
        messages.forEach(m -> m.read = true);

        conversations.addAll(getConversations(currentUserID));

        if (conversations.isEmpty()) return null;

        return new FriendConversationView(getFriendData(friendID), getFriendData(currentUserID), messages,
                conversations);
    }

    @Override
    public boolean newConversation(int friendID, int currentUserID) {
        return false;
    }

    @Override
    public boolean closeConversation(int friendID, int currentUserID) {
        ArrayList<ConversationData> conversations = getConversations(currentUserID);
        return conversations.remove(new ConversationData(null, null, true, friendID, 0));
    }

    private final HashMap<Integer, ArrayList<ConversationData>> conversations = new HashMap<>();

    private ArrayList<ConversationData> getConversations(int loggedID) {
        if (this.conversations.containsKey(loggedID))
            return this.conversations.get(loggedID);

        ArrayList<ConversationData> conversations = new ArrayList<>();

        if (loggedID == 23) {
            conversations.add(new ConversationData("/avatars/24.png", "Raphik2", true, 24,
                    getUnreadMessagesCount(24, loggedID)));
            conversations.add(new ConversationData("/avatars/27.png", "Raphistro", false, 27,
                    getUnreadMessagesCount(27, loggedID)));
        }

        this.conversations.put(loggedID, conversations);
        return this.conversations.get(loggedID);
    }

    private int getUnreadMessagesCount(int friendID, int loggedID) {
        ArrayList<MessageData> messages = getUserMessagesWith(friendID, loggedID);
        int count = 0;
        for (MessageData d: messages) {
            if (!d.read) count++;
        }
        // System.out.println("v ("+friendID+","+loggedID+")="+count);
        return count;
    }

    private final HashMap<Point2D, ArrayList<MessageData>> messages = new HashMap<>();

    private ArrayList<MessageData> getUserMessagesWith(int friendID, int loggedID) {
        Point2D key = new Point2D(friendID, loggedID);
        if (this.messages.containsKey(key))
            return this.messages.get(key);

        ArrayList<MessageData> messages = new ArrayList<>();

        if (friendID == 24 && loggedID == 23) {
            messages.add(
                    new MessageData(
                            23,
                            "java.lang.NoSuchMethodException: com.lgs.eden.views.achievements.\nAchievements.<init>()",
                            MessageType.TEXT,
                            Date.from(Instant.now()),
                            true
                    )
            );
            messages.add(new MessageData(24, "new Achievements()", MessageType.TEXT, Date.from(Instant.now()), false));
            messages.add(new MessageData(24, "new 2()", MessageType.TEXT, Date.from(Instant.now()), false));
            messages.add(new MessageData(24, "new 3()", MessageType.TEXT, Date.from(Instant.now()), false));
            messages.add(new MessageData(24, "new 4()", MessageType.TEXT, Date.from(Instant.now()), false));
            messages.add(new MessageData(24, "new 5()", MessageType.TEXT, Date.from(Instant.now()), false));
            messages.add(new MessageData(24, "new 6()", MessageType.TEXT, Date.from(Instant.now()), false));
            messages.add(new MessageData(24, "new 7()", MessageType.TEXT, Date.from(Instant.now()), false));
            messages.add(new MessageData(24, "new 8()", MessageType.TEXT, Date.from(Instant.now()), false));
            messages.add(new MessageData(24, "new 9()", MessageType.TEXT, Date.from(Instant.now()), false));
            messages.add(new MessageData(24, "new 10()", MessageType.TEXT, Date.from(Instant.now()), false));
            messages.add(new MessageData(24, "new 11()", MessageType.TEXT, Date.from(Instant.now()), false));
            messages.add(new MessageData(24, "new 12()", MessageType.TEXT, Date.from(Instant.now()), false));
            messages.add(new MessageData(24, "new 13()", MessageType.TEXT, Date.from(Instant.now()), false));
            messages.add(new MessageData(24, "new 14()", MessageType.TEXT, Date.from(Instant.now()), false));
            messages.add(new MessageData(24, "new 215()", MessageType.TEXT, Date.from(Instant.now()), false));
        }

        this.messages.put(key, messages);
        return this.messages.get(key);
    }

    // ------------------------------ UTILS ----------------------------- \\

    private FriendData getFriendData(int userID) {
        switch (userID){
            case 23: return new FriendData("/avatars/23.png", "Raphik", false, 23, FriendShipStatus.FRIENDS);
            case 24: return new FriendData("/avatars/24.png", "Raphik2", false, 24, FriendShipStatus.FRIENDS);
            case 25: return new FriendData("/avatars/25.png", "Calistral", false, 25, FriendShipStatus.FRIENDS);
            case 26: return new FriendData("/avatars/26.png", "Caliki", false, 26, FriendShipStatus.FRIENDS);
            case 27: return new FriendData("/avatars/27.png", "Raphistro", false, 27, FriendShipStatus.FRIENDS);
        }
        throw new IllegalArgumentException("not supported userID");
    }

    private FriendShipStatus evaluateRelationShip(int userID, int loggedID) {
        if(loggedID == userID) return FriendShipStatus.USER;

        boolean one = inFriendList(userID, loggedID);
        boolean two = inFriendList(loggedID, userID);

        if (one && two) return FriendShipStatus.FRIENDS;
        if (one) return FriendShipStatus.GOT_REQUESTED;
        if (two) return FriendShipStatus.REQUESTED;

        return FriendShipStatus.NONE;
    }

    private boolean inFriendList(int userID, int loggedID){
        ArrayList<FriendData> friendList = getFriendList(userID, loggedID);
        return friendList.contains(new FriendData(null, null, false, loggedID, FriendShipStatus.NONE));
    }
}
