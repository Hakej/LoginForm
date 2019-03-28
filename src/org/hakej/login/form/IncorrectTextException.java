package org.hakej.login.form;

public class IncorrectTextException extends Exception {
    public IncorrectTextException(String errorMessage) {
        super(errorMessage);
    }
}