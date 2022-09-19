package ru.otus;

public class AtmException extends RuntimeException{
    public AtmException() {
    }

    public AtmException(String message) {
        super(message);
    }
}