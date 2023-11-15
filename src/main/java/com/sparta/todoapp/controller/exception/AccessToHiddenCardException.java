package com.sparta.todoapp.controller.exception;

public class AccessToHiddenCardException extends RuntimeException{
    public AccessToHiddenCardException(String message) {
            super(message);
        }
}
