package de.othr.sw.pumpal.repository;

import de.othr.sw.pumpal.entity.Friendship;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepository extends PagingAndSortingRepository<Friendship, Long> {
}
