package com.hearthlogs.server.controller;

import com.hearthlogs.server.hearthpwn.CardParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//@Controller
public class CardImageDownloadController {

    @Autowired
    private CardParser cardParser;

//    @RequestMapping("/download")
    public String download() throws Exception {

        cardParser.downloadImages();

        return "Download complete";
    }
}
