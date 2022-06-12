package de.othr.sw.pumpal.repository;

import de.othr.sw.pumpal.entity.Visibility;
import de.othr.sw.pumpal.entity.Workout;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface WorkoutRepository extends PagingAndSortingRepository<Workout, Long> {

    List<Workout> findFirst5ByVisibilityInOrderByDateDesc(Collection<Visibility> visibilities);

}
