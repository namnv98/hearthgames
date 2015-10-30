package com.hearthlogs.server.controller;

import com.hearthlogs.server.config.security.UserInfo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleSecuredController {

    @RequestMapping("/test")
    public String test() {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "Welcome, " + userInfo.getBattletag();
    }
}