package com.sparta.todoapp.controller.exception;

public class BadAccessToCardException extends RuntimeException{
    public BadAccessToCardException(String message) {
            super(message);
        }
}
