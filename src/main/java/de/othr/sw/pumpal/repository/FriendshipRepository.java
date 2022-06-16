package de.othr.sw.pumpal.repository;

import de.othr.sw.pumpal.entity.Friendship;
import de.othr.sw.pumpal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query("SELECT f FROM Friendship as f WHERE f.requestingUser.email IN :user AND f.requestedUser IN :user")
    Friendship getFriendshipOfUsers(@Param("user")Collection<String> users);

    @Query("SELECT f.requestingUser FROM Friendship as f WHERE f.requestedUser.email LIKE %:email% AND f.isActive = TRUE")
    List<User> getActiveFriendshipsIncoming(@Param("email") String email);

    @Query("SELECT f.requestedUser FROM Friendship as f WHERE f.requestingUser.email LIKE %:email% AND f.isActive = TRUE")
    List<User> getActiveFriendshipsOutgoing(@Param("email") String email);
}
