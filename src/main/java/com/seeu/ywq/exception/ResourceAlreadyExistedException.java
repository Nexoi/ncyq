package com.seeu.ywq.exception;

/**
 * 资源已经存在，不可重复添加时使用
 */
public class ResourceAlreadyExistedException extends Exception {
    public ResourceAlreadyExistedException(String message) {
        super(message);
    }
}
