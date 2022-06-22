package de.othr.sw.pumpal.repository;

import de.othr.sw.pumpal.entity.Comment;
import de.othr.sw.pumpal.entity.Workout;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {

    List<Comment> findAllByWorkoutEqualsOrderByDate(Workout workout);
}
