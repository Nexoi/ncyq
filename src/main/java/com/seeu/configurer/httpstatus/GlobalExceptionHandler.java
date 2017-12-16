package com.seeu.configurer.httpstatus;

import com.seeu.core.R;
import com.seeu.file.storage.StorageFileNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一异常处理
 * <p>
 * Created by neo on 24/09/2017.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StorageFileNotFoundException.class)
//    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "无此文件")
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(R.code(404).message("[404] No such file").build());
    }

//    @ExceptionHandler(NullPointerException.class)
//    public ResponseEntity<?> handleNullPointerException(NullPointerException exc) {
//        return ResponseEntity.badRequest().body("空指针异常");// TODO
//    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException exc) {
        return ResponseEntity.badRequest().body(R.code(400).message("请确认传入参数是否完整 [ " + exc.getMessage() + " ]").build());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exc) {
        return ResponseEntity.badRequest().body(R.code(400).message("请确认传入参数类型是否正确 [ " + exc.getMessage() + " ]").build());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handleNoHandlerFoundException(NoHandlerFoundException exc) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(R.code(404).message("接口 {[" + exc.getHttpMethod() + "]" + exc.getRequestURL() + "} 不存在").build());
    }

//    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpServletRequest request, HttpRequestMethodNotSupportedException exc) {
//        return ResponseEntity.badRequest().body("不支持该请求类型 [" + request.getMethod() + "]");
//    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleOtherException(HttpServletRequest request, Exception exc) {
//        return ResponseEntity.badRequest().body("接口 {[" + request.getMethod() + "]" + request.getRequestURI() + "} 内部错误，请联系管理员");
//    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<?> handleEmptyResultDataAccessException(HttpServletRequest request, EmptyResultDataAccessException exc) {
        return ResponseEntity.badRequest().body(R.code(400).message("no such data found").build());
    }
}
