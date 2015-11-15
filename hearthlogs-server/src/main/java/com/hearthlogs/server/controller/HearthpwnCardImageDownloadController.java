package com.hearthlogs.server.controller;

import com.hearthlogs.server.util.HearthPwnCardParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HearthpwnCardImageDownloadController {

    @Autowired
    private HearthPwnCardParser cardParser;

    @RequestMapping("/download")
    public String download() throws Exception {

        cardParser.downloadImages();

        return "Download complete";
    }
}
