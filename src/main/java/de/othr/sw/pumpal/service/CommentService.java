package de.othr.sw.pumpal.service;

import de.othr.sw.pumpal.entity.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> getAllCommentsOfWorkout(Long workoutId); //Workout workoutid?

    int getNumberOfComments(Long workoutId);

    void addComment(Comment comment); //workoutid/userid wird vorher über model in comment gesetzt

    void deleteComment(Comment comment); //überlegen, ob Comment ID ausreicht oder Comment übergeben??
}
