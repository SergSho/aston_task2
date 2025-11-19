package ru.sergeyshokhin.exception;

public class AppException extends Exception {
    public AppException(String message, Exception e) {
        super(message, e);
    }

    public AppException(Exception e) {
        super(e);
    }
}
