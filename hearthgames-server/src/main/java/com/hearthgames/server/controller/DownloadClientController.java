package com.hearthgames.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DownloadClientController {

    @RequestMapping(value = "/download")
    public String downloadClient() {
        return "download";
    }

}
