package de.othr.sw.pumpal.service;

import de.othr.sw.pumpal.entity.Friendship;
import de.othr.sw.pumpal.entity.User;

import java.util.List;

public interface FriendshipService {

    //ausbessern in Komponentendia: getFriendsOfUser steht da bisher
    List<Friendship> getAllFriendsOfUser(User user); //userId statt User objekt?

    List<Friendship> getAllIncomingFriendRequestsOfUser(User user);

    List<Friendship> getAllOutgoingFriendRequestsOfUser(User user);

    void sendFriendRequest(User requesting, User requested); //create inactive Friendship,  maybe auch eine Funktion für Anfrage zurückziehen?

    void acceptFriendRequest(Friendship friendship); //isActive auf True setzen

    void denyFriendRequest(Friendship friendship); //remove Friendrequest

    void deleteFriend(Friendship friendship); //remove Friendship



}
