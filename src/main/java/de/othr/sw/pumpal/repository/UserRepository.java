package de.othr.sw.pumpal.repository;

import de.othr.sw.pumpal.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, String> {

    Page<User> findAllByEmailNotLike(String email, Pageable paging);

}
