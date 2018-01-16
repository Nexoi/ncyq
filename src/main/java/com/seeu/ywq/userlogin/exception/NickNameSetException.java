package com.seeu.ywq.userlogin.exception;

public class NickNameSetException extends Exception {
    public NickNameSetException(String name) {
        super("NickName set exception [ " + name + " ]");
    }
}
