package com.hearthgames.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SitemapController {

    @RequestMapping(value = "/sitemap")
    public String sitemap() {
        return "sitemap";
    }
}
