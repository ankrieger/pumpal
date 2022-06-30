package de.othr.sw.pumpal.service;

import de.othr.sw.pumpal.entity.Friendship;
import de.othr.sw.pumpal.entity.User;

import java.util.List;

public interface FriendshipService {

    //ausbessern in Komponentendia: getFriendsOfUser steht da bisher
    List<User> getAllFriendsOfUser(User user);

    List<User> getAllIncomingFriendRequestsOfUser(User user);

    List<User> getAllOutgoingFriendRequestsOfUser(User user);

    String getStatusOfFriendship(User requesting, User requested);

    void sendFriendRequest(User requesting, User requested);

    void acceptFriendRequest(Friendship friendship);

    void deleteFriendship(Friendship friendship);

    Friendship getFriendshipOfUsers(User requesting, User requested);

}
