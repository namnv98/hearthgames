package com.hearthgames.utils.hearthpwn.controller;

import com.hearthgames.utils.hearthpwn.CardDetails;
import com.hearthgames.utils.hearthpwn.CardParser;
import com.hearthgames.utils.hearthpwn.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
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
            String css = "." + cd.getId() + " { background-image: url(\"http://images.hearthgames.com/" + cd.getId() +".png\"); }";
            detailsCss.add(css);
            if (cd.getId().startsWith("HERO")) {
                css = "." + cd.getId() + "_large { background-image: url(\"http://images.hearthgames.com/" + cd.getId() +"_large.png\"); }";
                detailsCss.add(css);
            }
        }

        modelAndView.addObject("detailsCss", detailsCss);

        return modelAndView;
    }

}
