package com.seeu.ywq._web;

import com.seeu.ywq._web.model.WebPageActivity;
import com.seeu.ywq._web.service.WebPageActivityService;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.version.model.AppVersion;
import com.seeu.ywq.version.service.AppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private WebPageActivityService webPageActivityService;
    @Value("${qiniu.protocol_host}")
    private String host;
    @Autowired
    private AppVersionService appVersionService;

    @RequestMapping("/trend/{publishId}")
    public String page(@PathVariable Long publishId, ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) {
//        modelAndView.addObject();
        String protocol = request.getProtocol();
        String host = request.getHeader("host");

        return "page/trend";
    }

    @GetMapping("/activity/{id}")
    public String activity(@PathVariable Long id, Model model) {
        try {
            WebPageActivity activity = webPageActivityService.findOne(id);
            model.addAttribute("model", activity);
            return "activity";
        } catch (ResourceNotFoundException e) {
            return "404";
        }
    }

    @GetMapping("/register")
    public String register(@RequestParam(required = false) Long invite, Model model) {
        if (invite == null)
            model.addAttribute("invitedUid", 0L);
        else
            model.addAttribute("invitedUid", invite);
        return "register";
    }


    /**
     * 下载 APP
     */
    @Value("${ywq.ios_download_url}")
    private String iosUrl;

    @GetMapping("/download")
    public String download(HttpServletRequest request) {
        String user = request.getHeader("User-Agent");
        if (user != null) {
            user = user.toUpperCase();
            if (user.contains("APPLE") || user.contains("IOS") || user.contains("SAFARI")) {
                return "redirect:" + iosUrl;
            }
        }
        // 其余情况都下载 apk
        String androidUrl;
        AppVersion version = appVersionService.getNewVersion(AppVersion.CLIENT.ANDROID);
        if (version == null || version.getDownloadUrl() == null)
            androidUrl = host + "/" + "youwuquan.apk";
        else
            androidUrl = version.getDownloadUrl();
//        if (user == null)
//            return "redirect:" + androidUrl;
//        user = user.toUpperCase();
//        if (user.contains("ANDROID") || user.contains("CHROME"))
//            return "redirect:" + androidUrl;
        return "redirect:" + androidUrl;
    }
}
