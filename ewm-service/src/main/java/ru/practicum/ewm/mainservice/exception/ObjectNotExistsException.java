package ru.practicum.ewm.mainservice.exception;

public class ObjectNotExistsException extends RuntimeException {
    public ObjectNotExistsException(String message) {
        super(message);
    }
}
