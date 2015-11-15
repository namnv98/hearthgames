package com.hearthlogs.server.util;

import com.hearthlogs.server.match.parse.domain.CardDetails;
import com.hearthlogs.server.service.CardService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HearthPwnCardParser {

    @Autowired
    private CardService cardService;

    public List<HearthPwnCardLink> getCardLinks() throws IOException {

        List<HearthPwnCardLink> cardLinks = new ArrayList<>();
        for (int i=1; i < 16; i++) {
            String url = "http://www.hearthpwn.com/cards?display=1&sort=name&page=";

            Document doc = Jsoup.connect(url+i).userAgent("Mozilla").get();
            Element row = doc.getElementById("cards");
            Elements rows = row.getElementsByTag("tr");


            Map<String, String> namesFound = new HashMap<>();

            for (Element element : rows) {
                Elements link = element.select("td > a");
                if (link.size() == 1) {
                    String href = link.get(0).attr("href");
                    String name = link.get(0).html();

                    CardDetails cardDetails;
                    if (namesFound.containsKey(name)) {
                        cardDetails = cardService.getByName(name);
                        cardDetails = cardService.getCardDetails(cardDetails.getId()+"H");
                    } else {
                        cardDetails = cardService.getByName(name);
                        namesFound.put(name, "1");
                    }


                    String cardId = "";
                    if (cardDetails != null) {
                        cardId = cardDetails.getId();
                    }
                    HearthPwnCardLink cardLink = new HearthPwnCardLink();
                    cardLink.setCardId(cardId);
                    cardLink.setHref(href);
                    cardLink.setName(name);
                    cardLinks.add(cardLink);
                }
            }
        }
        return cardLinks;
    }
}
