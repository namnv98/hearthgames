package com.hearthlogs.server.controller;

import com.hearthlogs.server.config.security.UserInfo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping(value = "/")
    public String home() {
//        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return "Welcome, " + userInfo.getBattletag();

        return "home";
    }
}
