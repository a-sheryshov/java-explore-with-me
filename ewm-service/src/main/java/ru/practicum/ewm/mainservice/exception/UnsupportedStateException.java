package ru.practicum.ewm.mainservice.exception;

public class UnsupportedStateException extends RuntimeException {
    public UnsupportedStateException(String message) {
        super(message);
    }
}
