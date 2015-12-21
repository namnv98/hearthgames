package com.hearthgames.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FAQController {

    @RequestMapping(value = "/faq")
    public String faq() {
        return "faq";
    }

}
