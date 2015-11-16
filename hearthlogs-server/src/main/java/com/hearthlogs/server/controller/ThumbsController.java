package com.hearthlogs.server.controller;

import com.hearthlogs.server.util.HearthPwnCardParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class ThumbsController {

    @Autowired
    private HearthPwnCardParser cardParser;

    @RequestMapping("/thumbs")
    public String thumbs() throws IOException {

        cardParser.makeThumbnails();

        return "thumbs";
    }

}
