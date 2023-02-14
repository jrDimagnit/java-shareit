package ru.practicum.shareit.error;

public class BadParametersException extends RuntimeException {
    public BadParametersException(String message) {
        super(message);
    }
}
