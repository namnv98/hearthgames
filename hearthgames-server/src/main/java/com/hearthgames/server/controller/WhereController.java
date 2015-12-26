package com.hearthgames.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WhereController {

    @RequestMapping(value = "/where")
    public ModelAndView where(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("where");

        String userAgent = request.getHeader("User-Agent");
        if (userAgent != null) {
            if (userAgent.toLowerCase().contains("windows")) {
                modelAndView.addObject("userAgent", new UserAgent(true, false, false));
            } else if (userAgent.toLowerCase().contains("mac")) {
                modelAndView.addObject("userAgent", new UserAgent(false, true, false));
            } else if (userAgent.toLowerCase().contains("android")) {
                modelAndView.addObject("userAgent", new UserAgent(false, false, true));
            } else {
                modelAndView.addObject("userAgent", new UserAgent(true, false, false));
            }
        } else {
            modelAndView.addObject("userAgent", new UserAgent(true, false, false));
        }

        return modelAndView;
    }

    public static class UserAgent {

        private boolean windows;
        private boolean mac;
        private boolean android;

        public UserAgent(boolean windows, boolean mac, boolean android) {
            this.windows = windows;
            this.mac = mac;
            this.android = android;
        }

        public boolean isWindows() {
            return windows;
        }

        public void setWindows(boolean windows) {
            this.windows = windows;
        }

        public boolean isMac() {
            return mac;
        }

        public void setMac(boolean mac) {
            this.mac = mac;
        }

        public boolean isAndroid() {
            return android;
        }

        public void setAndroid(boolean android) {
            this.android = android;
        }
    }

}
