package com.hearthlogs.server.hearthpwn.controller;

import com.hearthlogs.server.game.parse.domain.CardDetails;
import com.hearthlogs.server.hearthpwn.CardParser;
import com.hearthlogs.server.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@PreAuthorize("hasRole('ADMIN')")
public class ThumbsController {

    @Autowired
    private CardParser cardParser;

    @Autowired
    private CardService cardService;

    @RequestMapping("/thumbs")
    public String thumbs() throws IOException {

        cardParser.makeThumbnails();

        return "thumbs";
    }

    @RequestMapping("/thumbscss")
    public ModelAndView thumbscss() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("thumbscss");

        List<String> detailsCss = new ArrayList<>();
        Collection<CardDetails> cardDetails  = new ArrayList<>();
        for (CardDetails cd: cardService.getCardDetails()) {
            String css = "." + cd.getId() + " { background-image: url(\"http://images.hearthlogs.com/" + cd.getId() +".png\"); }";
            detailsCss.add(css);
            if (cd.getId().startsWith("HERO")) {
                css = "." + cd.getId() + "_large { background-image: url(\"http://images.hearthlogs.com/" + cd.getId() +"_large.png\"); }";
                detailsCss.add(css);
            }
        }

        modelAndView.addObject("detailsCss", detailsCss);

        return modelAndView;
    }

}
