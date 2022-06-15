package de.othr.sw.pumpal.repository;

import de.othr.sw.pumpal.entity.Friendship;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface FriendshipRepository extends PagingAndSortingRepository<Friendship, Long> {

    @Query("SELECT f FROM Friendship as f WHERE f.requestingUser.email IN :user AND f.requestedUser IN :user")
    Friendship getFriendshipOfUsers(@Param("user")Collection<String> users);
}
