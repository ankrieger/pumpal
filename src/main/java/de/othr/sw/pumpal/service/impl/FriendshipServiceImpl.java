package de.othr.sw.pumpal.service.impl;

import de.othr.sw.pumpal.entity.Friendship;
import de.othr.sw.pumpal.entity.User;
import de.othr.sw.pumpal.repository.FriendshipRepository;
import de.othr.sw.pumpal.service.FriendshipService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class FriendshipServiceImpl implements FriendshipService {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    Logger logger;


    @Override
    public List<User> getAllFriendsOfUser(User user) {
        if(user!=null) {
            List<User> friends = friendshipRepository.getFriendshipsIncoming(user.getEmail(), true); //active friendships
            friends.addAll(friendshipRepository.getFriendshipsOutgoing(user.getEmail(), true)); //active friendships
            logger.info("Got friends of user " + user.getEmail());
            return friends;
        }
        return null;
    }

    @Override
    public List<User> getAllIncomingFriendRequestsOfUser(User user) {
        if (user!=null) {
            logger.info("Got received friendrequests of user " + user.getEmail());
            return friendshipRepository.getFriendshipsIncoming(user.getEmail(), false);
        }
        return null;
    }

    @Override
    public List<User> getAllOutgoingFriendRequestsOfUser(User user) {
        if(user!=null) {
            logger.info("Got sent friendrequests of user " + user.getEmail());
            return friendshipRepository.getFriendshipsOutgoing(user.getEmail(), false);
        }
        return null;
    }

    @Override
    public String getStatusOfFriendship(User requesting, User requested) {
        Friendship friendship = this.getFriendshipOfUsers(requesting,requested);

        if (friendship == null) return "none";
        else if (friendship.isActive()) return "friends";
        else if (friendship.getRequesting().getEmail().equals(requesting.getEmail())) return "friendRequesting";
        else return "friendRequested";
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendFriendRequest(User requesting, User requested) {
        if (requesting!=null && requested!=null && !requesting.equals(requested)) {
            Friendship friendship = new Friendship();
            friendship.setDate(Timestamp.valueOf(LocalDateTime.now()));
            friendship.setRequesting(requesting);
            friendship.setRequested(requested);
            friendship.setActive(false);

            friendshipRepository.save(friendship);
            logger.info("Friendrequest from user " + requesting.getEmail() + " to user " + requested.getEmail() + " was sent.");
        }
    }

    @Override
    @Transactional( propagation = Propagation.REQUIRES_NEW)
    public void acceptFriendRequest(Friendship friendship) {
        Optional<Friendship> friendshipUpdate = friendshipRepository.findById(friendship.getId());
        friendshipUpdate.ifPresent(friendsUpdate -> {
            friendship.setActive(true);
            friendshipRepository.save(friendship);
            logger.info("Friendship was accepted.");
        });
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteFriendship(Friendship friendship) {
        Optional<Friendship> friendshipDelete = friendshipRepository.findById(friendship.getId());
        friendshipDelete.ifPresent(friendshipDel -> {
            friendshipRepository.delete(friendship);
            logger.info("Friendship was removed.");
        });
    }

    @Override
    public Friendship getFriendshipOfUsers(User requesting, User requested) {
        if (requesting!=null && requested!=null) {
            Collection<String> users = new ArrayList<>();
            users.add(requesting.getEmail());
            users.add(requested.getEmail());
            return friendshipRepository.getFriendshipOfUsers(users);
        }
        return null;
    }


}
