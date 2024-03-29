package de.othr.sw.pumpal.repository;

import de.othr.sw.pumpal.entity.Level;
import de.othr.sw.pumpal.entity.User;
import de.othr.sw.pumpal.entity.Visibility;
import de.othr.sw.pumpal.entity.Workout;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, String> {


    @Query("SELECT u FROM User u WHERE concat(u.email,u.firstName,u.name) LIKE %:filter%")
    Page<User> getUserByKeyword(@Param("filter") String filter, Pageable paging);


    @Query("SELECT u FROM User u JOIN u.savedWorkouts w WHERE w.id=:id")
    List<User> getAllUsersSavingWorkoutWithId(@Param("id") Long id);

}
