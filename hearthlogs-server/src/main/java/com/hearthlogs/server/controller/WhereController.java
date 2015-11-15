package com.hearthlogs.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WhereController {

    @RequestMapping(value = "/where")
    public String where() {
        return "where";
    }

}
