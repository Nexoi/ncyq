package com.seeu.configurer;

import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by neoxiaoyi.
 * User: neo
 * Date: 08/12/2017
 * Time: 1:37 AM
 * Describe:
 * <p>
 * 将所有的请求返回数据都封装为标准 JSON + 详细引导 格式
 * <p>
 * {
 * "timestamp": 1512668717923,
 * "status": 400,
 * "error": "Bad Request",
 * "exception": "org.springframework.validation.BindException",
 * "errors": [
 * {
 * "codes": [
 * "NotNull.productCategory.name",
 * "NotNull.name",
 * "NotNull.java.lang.String",
 * "NotNull"
 * ],
 * "arguments": [
 * {
 * "codes": [
 * "productCategory.name",
 * "name"
 * ],
 * "arguments": null,
 * "defaultMessage": "name",
 * "code": "name"
 * }
 * ],
 * "defaultMessage": "may not be null",
 * "objectName": "productCategory",
 * "field": "name",
 * "rejectedValue": null,
 * "bindingFailure": false,
 * "code": "NotNull"
 * }
 * ],
 * "message": "Validation failed for object='productCategory'. Error count: 1",
 * "path": "/api/admin/v2/product-category/1"
 * }
 */
@ControllerAdvice
public class YWQResponseBodyAdvice implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        // 除了 /error 全部捕获
        String methodName = methodParameter.getMethod().getName();
//        return methodName.startsWith("/api/") &&
//        return !aClass.getName().equals("Swagger2Controller") &&
//                !aClass.getName().equals("ApiResourceController") &&
//                !(methodParameter.getMethod().getName().equalsIgnoreCase("securityConfiguration")) &&
//                !(methodParameter.getMethod().getName().equalsIgnoreCase("getDocumentation")) &&
//                !(methodParameter.getMethod().getName().equalsIgnoreCase("uiConfiguration")) &&
//                !(methodParameter.getMethod().getName().equalsIgnoreCase("swaggerResources")) &&
//        return !(methodParameter.getMethod().getName().equalsIgnoreCase("error"));
        return false;
//        return !(methodParameter.getMethod().getName().equalsIgnoreCase("loadFile"));
    }

    @Override
    public Object beforeBodyWrite(Object returnValue, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
//        if (methodParameter.getMethod().getName().equals("error")) // 这是 error 错误页面，建议以后单独再封装一次 error
//            return returnValue;
        serverHttpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        HttpServletResponse response = ((ServletServerHttpResponse) serverHttpResponse).getServletResponse();
        Map map = new HashMap();
        map.put("timestamp", new Date().getTime());
        map.put("status", response.getStatus());
        map.put("path", serverHttpRequest.getURI().getPath());
        map.put("version", "1.0");
        if (response.getStatus() == 200) {
            map.put("data", returnValue);
        } else {
            map.put("message", returnValue);
        }
        if (mediaType.includes(MediaType.APPLICATION_JSON))
            return JSONObject.toJSON(map); // 利用 fastJSON convert 数据
        else
            return JSONObject.toJSON(map).toString(); // 根据 mediaType 的不同，Spring 会在 AbstractMessageConverterMethodProcessor#writeWithMessageConverters() 中产生不同的结果，如果 type 为 plain text 则会无法 convert json 文本
    }
}
