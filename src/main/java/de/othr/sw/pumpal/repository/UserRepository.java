package de.othr.sw.pumpal.repository;

import de.othr.sw.pumpal.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, String> {

}
