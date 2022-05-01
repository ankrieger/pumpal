package de.othr.sw.pumpal.repository;

import de.othr.sw.pumpal.entity.Exercise;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends PagingAndSortingRepository<Exercise, Long> {
}
