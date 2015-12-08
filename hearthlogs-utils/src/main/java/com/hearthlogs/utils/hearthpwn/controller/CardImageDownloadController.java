package com.hearthlogs.utils.hearthpwn.controller;

import com.hearthlogs.utils.hearthpwn.CardParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PreAuthorize("hasRole('ADMIN')")
public class CardImageDownloadController {

    @Autowired
    private CardParser cardParser;

    @RequestMapping("/download")
    public String download() throws Exception {

        cardParser.downloadImages();

        return "Download complete";
    }
}
