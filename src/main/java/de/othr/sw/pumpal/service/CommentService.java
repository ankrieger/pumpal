package de.othr.sw.pumpal.service;

import de.othr.sw.pumpal.entity.Comment;
import de.othr.sw.pumpal.entity.User;
import de.othr.sw.pumpal.entity.Workout;

import java.util.List;

public interface CommentService {

    List<Comment> getAllCommentsOfWorkout(Workout workout); //Workout workoutid?

    void addComment(Comment comment, User user, Workout workout); //workoutid/userid wird vorher über model in comment gesetzt

    void deleteComment(Comment comment); //überlegen, ob Comment ID ausreicht oder Comment übergeben??
}
