package de.othr.sw.pumpal.repository;

import de.othr.sw.pumpal.entity.User;
import de.othr.sw.pumpal.entity.Visibility;
import de.othr.sw.pumpal.entity.Workout;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface WorkoutRepository extends PagingAndSortingRepository<Workout, Long> {

    List<Workout> findAllByAuthorAndVisibility(User user, Visibility visibility);

    List<Workout> findFirst5ByVisibilityInOrderByDateDesc(Collection<Visibility> visibilities);

    List<Workout> findAllByVisibilityInOrderByDateDesc(Collection<Visibility> visibilities);

    Page<Workout> findAllByVisibilityInOrderByDateDesc(Collection<Visibility> visibilities, Pageable paging);


    //List<Workout> findAllByVisibilityInAndAuthorIn(Collection<Visibility> visibilities, Collection<User> users);

    //@Query("SELECT w FROM Workout AS w WHERE w.visibility IN :visibilities and w.author.email IN :emails")
    //List<Workout> findAllByVisibilityInAndAuthor(@Param("visibilities") Collection<Visibility> visibilities, @Param("emails") Collection<String> friends);

}
