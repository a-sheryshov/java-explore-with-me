package ru.practicum.ewm.mainservice.exception;

public class InvalidMethodParameterException extends RuntimeException {
    public InvalidMethodParameterException(String message) {
        super(message);
    }
}
