package com.hearthlogs.server.controller;

import com.hearthlogs.server.database.domain.BetaSignUp;
import com.hearthlogs.server.database.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class SignUpController {

    @Autowired
    private GameService gameService;

    @RequestMapping(value = "/signup")
    public ModelAndView signup(BetaSignUp betaSignUp) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("signup");
        if (!StringUtils.isEmpty(betaSignUp.getBattletag()) && !StringUtils.isEmpty(betaSignUp.getEmail())) {
            gameService.signUp(betaSignUp);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/signedup")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView signedup() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("signedup");
        List<BetaSignUp> betaSignUps = gameService.getSignupsToBeApproved();
        modelAndView.addObject("signups", betaSignUps);
        return modelAndView;
    }

}
