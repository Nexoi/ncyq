package com.seeu.core;

import java.util.Date;

/**
 * Response 消息返回（不支持数据返回）
 * 例如：
 * 当发生 400 错误的时候，请使用此消息体进行数据返回
 */
public class R {

    public static ResponseBuilder code(Integer code) {
        return new ResponseBuilder(code);
    }

    public static class ResponseBuilder {
        private String message;
        private Integer code;
        private Date timestamp;

        public String getMessage() {
            return message;
        }

        public Integer getCode() {
            return code;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public ResponseBuilder(Integer code) {
            this.code = code;
            this.timestamp = new Date();
        }

        public ResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ResponseBuilder build() {
            return this;
        }
    }
}