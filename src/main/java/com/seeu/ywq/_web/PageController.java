package com.seeu.ywq._web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

        return "page/trend";
    }


    @GetMapping("/register")
    public String register(@RequestParam(required = false) Long invite, Model model) {
        if (invite == null)
            model.addAttribute("invitedUid", 0L);
        else
            model.addAttribute("invitedUid", invite);
        return "register";
    }
}
