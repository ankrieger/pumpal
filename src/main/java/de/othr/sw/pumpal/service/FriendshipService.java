package de.othr.sw.pumpal.service;

import de.othr.sw.pumpal.entity.Friendship;
import de.othr.sw.pumpal.entity.User;

import java.util.List;

public interface FriendshipService {

    //ausbessern in Komponentendia: getFriendsOfUser steht da bisher
    List<User> getAllFriendsOfUser(User user); //userId statt User objekt?

    List<Friendship> getAllIncomingFriendRequestsOfUser(User user);  //maybe noch so anzahl an ... damit dann "Nachrichtenbutton" konstruieren kann?

    List<Friendship> getAllOutgoingFriendRequestsOfUser(User user);

    String getStatusOfFriendship(User requesting, User requested);

    Friendship sendFriendRequest(User requesting, User requested); //create inactive Friendship,  maybe auch eine Funktion für Anfrage zurückziehen?

    void acceptFriendRequest(Friendship friendship); //isActive auf True setzen

    void deleteFriendship(Friendship friendship); //remove Friendship

    Friendship getFriendshipOfUsers(User requesting, User requested);

}
