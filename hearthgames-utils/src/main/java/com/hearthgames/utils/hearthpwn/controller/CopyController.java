package com.hearthgames.utils.hearthpwn.controller;

import com.hearthgames.utils.hearthpwn.CardDetails;
import com.hearthgames.utils.hearthpwn.CardParser;
import com.hearthgames.utils.hearthpwn.CardService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CopyController {

    @Autowired
    private CardService cardService;

    @RequestMapping("/copy")
    public String copy() throws IOException {

        List<CardDetails> heros = cardService.getCardDetails().stream().filter(cardDetails -> !"CHEAT".equals(cardDetails.getSet()) && !"CREDITS".equals(cardDetails.getSet()) && "HERO".equals(cardDetails.getType())).collect(Collectors.toList());

        for (CardDetails cardDetails: heros) {
            try {
                FileUtils.copyFile(new File("C:\\images\\download\\"+cardDetails.getId()+".png"), new File("C:\\images\\heroraw\\"+cardDetails.getId()+".png"));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        return "copy";
    }
}
