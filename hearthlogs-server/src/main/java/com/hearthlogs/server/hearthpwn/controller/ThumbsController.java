package com.hearthlogs.server.hearthpwn.controller;

import com.hearthlogs.server.hearthpwn.CardParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@PreAuthorize("hasRole('ADMIN')")
public class ThumbsController {

    @Autowired
    private CardParser cardParser;

    @RequestMapping("/thumbs")
    public String thumbs() throws IOException {

        cardParser.makeThumbnails();

        return "thumbs";
    }

}
