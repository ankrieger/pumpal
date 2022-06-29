package de.othr.sw.pumpal.service.exception;

public class WorkoutNotFoundException extends RuntimeException {
    public WorkoutNotFoundException(String id) {
        super("Workout doesn't exist with id: " + id);
    }
}
