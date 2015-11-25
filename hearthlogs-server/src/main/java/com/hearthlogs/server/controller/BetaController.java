package com.hearthlogs.server.controller;

import com.hearthlogs.server.database.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BetaController {

    @Autowired
    private GameService gameService;

    @RequestMapping(value = "/beta")
    public String beta() {
        return "beta";
    }

    @RequestMapping(value = "/betaapprove/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String betaapprove(@PathVariable Long id) {
        gameService.approve(id);
        return "redirect:/signedup";
    }

}
