package com.seeu.ywq.userlogin.exception;

public class PasswordSetException extends Exception {
    public PasswordSetException(String password) {
        super("Password set exception [ " + password + " ]");
    }
}
