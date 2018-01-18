package com.seeu.configurer.httpstatus;

import com.seeu.core.R;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HttpUnauthorizedController {

    @RequestMapping("/unauthorized")
    public ResponseEntity unauthorizedPage() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(R.code(401).message("无权访问 [unauthorized]").build());
    }
}
