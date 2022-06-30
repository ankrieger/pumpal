package de.othr.sw.pumpal.service.impl;

import de.othr.sw.pumpal.entity.Comment;
import de.othr.sw.pumpal.entity.User;
import de.othr.sw.pumpal.entity.Workout;
import de.othr.sw.pumpal.repository.CommentRepository;
import de.othr.sw.pumpal.service.CommentService;
import de.othr.sw.pumpal.service.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    Logger logger;

    @Override
    public List<Comment> getAllCommentsOfWorkout(Workout workout) {
        if(workout!=null) {
            return commentRepository.findAllByWorkoutEqualsOrderByDate(workout);
        }
        return null;
    }

    @Override
    @Transactional( propagation = Propagation.REQUIRES_NEW)
    public void addComment(Comment comment, User user, Workout workout) {
        if(user!=null && workout!=null) {
            comment.setAuthor(user);
            comment.setWorkout(workout);
            comment.setDate(Timestamp.valueOf(LocalDateTime.now()));

            System.out.println("Comment = " + comment);
            commentRepository.save(comment);
            logger.info("Comment by user " + user.getEmail() + " was added.");
        }
    }

    @Override
    @Transactional( propagation = Propagation.REQUIRES_NEW)
    public void deleteCommentById(Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentDel -> {
            commentRepository.deleteById(commentId);
            logger.info("Comment was deleted.");
        });
    }
}
