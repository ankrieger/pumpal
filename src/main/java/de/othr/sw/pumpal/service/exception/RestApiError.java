package de.othr.sw.pumpal.service.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class RestApiError {

    private String message;
    private HttpStatus status;
    private LocalDateTime timeStamp;


    public RestApiError(String message, HttpStatus status, LocalDateTime timeStamp) {
        this.message = message;
        this.status = status;
        this. timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
