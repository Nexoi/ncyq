package com.seeu.configurer.httpstatus;//package com.seeu.configurer.httpstatus;//package com.seeu.configurer.httpstatus;
//
//import com.seeu.core.R;
//import org.springframework.boot.autoconfigure.web.ErrorController;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * Created by neo on 24/09/2017.
// */
//@RestController
//public class HttpErrorController implements ErrorController {
//    private static final String ERROR_PATH = "/error";
//
//    @RequestMapping(ERROR_PATH)
//    public ResponseEntity errorPage(HttpServletRequest request) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("[error] no such resource found");
//    }
//
//    @Override
//    public String getErrorPath() {
//        return ERROR_PATH;
//    }
//}
