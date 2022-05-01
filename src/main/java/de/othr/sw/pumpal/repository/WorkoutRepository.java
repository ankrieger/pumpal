package de.othr.sw.pumpal.repository;

import de.othr.sw.pumpal.entity.Workout;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutRepository extends PagingAndSortingRepository<Workout, Long> {

}
