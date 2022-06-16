package de.othr.sw.pumpal.service.impl;

import de.othr.sw.pumpal.entity.Friendship;
import de.othr.sw.pumpal.entity.User;
import de.othr.sw.pumpal.repository.FriendshipRepository;
import de.othr.sw.pumpal.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class FriendshipServiceImpl implements FriendshipService {

    @Autowired
    private FriendshipRepository friendshipRepository;


    @Override
    public List<User> getAllFriendsOfUser(User user) {
        List<User> friends = friendshipRepository.getFriendshipsIncoming(user.getEmail(), true); //active friendships
        friends.addAll(friendshipRepository.getFriendshipsOutgoing(user.getEmail(), true)); //active friendships
        System.out.println(friends);
        return friends;
    }

    @Override
    public List<User> getAllIncomingFriendRequestsOfUser(User user) {
        return friendshipRepository.getFriendshipsIncoming(user.getEmail(), false);
    }

    @Override
    public List<User> getAllOutgoingFriendRequestsOfUser(User user) {
        return friendshipRepository.getFriendshipsOutgoing(user.getEmail(), false);
    }

    @Override
    public String getStatusOfFriendship(User requesting, User requested) {
        Friendship friendship = this.getFriendshipOfUsers(requesting,requested);

        System.out.println(friendship);

        if (friendship == null) return "none";
        else if (friendship.isActive()) return "friends";
        else if (friendship.getRequesting().getEmail().equals(requesting.getEmail())) return "friendRequesting";
        else return "friendRequested";
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Friendship sendFriendRequest(User requesting, User requested) {
        if (requesting.equals(requested)) return null; //throw exception

        Friendship friendship = new Friendship();
        friendship.setDate(Timestamp.valueOf(LocalDateTime.now()));
        friendship.setRequesting(requesting);
        friendship.setRequested(requested);
        friendship.setActive(false);

        System.out.println(friendship);

        return friendshipRepository.save(friendship);
    }

    @Override
    public void acceptFriendRequest(Friendship friendship) {
        friendship.setActive(true);
        friendshipRepository.save(friendship);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteFriendship(Friendship friendship) {
        friendshipRepository.delete(friendship);
    }

    @Override
    public Friendship getFriendshipOfUsers(User requesting, User requested) {
        Collection<String> users = new ArrayList<String>();
        users.add(requesting.getEmail());
        users.add(requested.getEmail());
        return friendshipRepository.getFriendshipOfUsers(users);
    }


}
