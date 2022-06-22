package de.othr.sw.pumpal.service.impl;

import de.othr.sw.pumpal.entity.Comment;
import de.othr.sw.pumpal.entity.User;
import de.othr.sw.pumpal.entity.Workout;
import de.othr.sw.pumpal.repository.CommentRepository;
import de.othr.sw.pumpal.service.CommentService;
import de.othr.sw.pumpal.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> getAllCommentsOfWorkout(Workout workout) {
        return commentRepository.findAllByWorkoutEqualsOrderByDate(workout);
    }

    @Override
    public void addComment(Comment comment, User user, Workout workout) {
        comment.setAuthor(user);
        comment.setWorkout(workout);
        comment.setDate(Timestamp.valueOf(LocalDateTime.now()));

        System.out.println("Comment = " + comment);
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }
}
