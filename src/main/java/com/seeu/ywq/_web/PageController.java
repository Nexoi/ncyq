package com.seeu.ywq._web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 无权限，公开访问
 */
@Controller
@RequestMapping("/")
public class PageController {

    @RequestMapping("/trend/{publishId}")
    public String page(@PathVariable Long publishId, ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) {
//        modelAndView.addObject();
        String protocol = request.getProtocol();
        String host = request.getHeader("host");

        return "/page/trend";
    }

}
