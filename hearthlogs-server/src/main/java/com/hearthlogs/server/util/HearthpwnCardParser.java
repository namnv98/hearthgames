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
import java.util.List;

@Component
public class HearthpwnCardParser {

    @Autowired
    private CardService cardService;

    public List<HearthpwnCardLink> getCardLinks() throws IOException {

        List<HearthpwnCardLink> cardLinks = new ArrayList<>();
        for (int i=1; i < 14; i++) {
            String url = "http://www.hearthpwn.com/cards?display=1&page=";

            Document doc = Jsoup.connect(url+i).userAgent("Mozilla").get();
            Element row = doc.getElementById("cards");
            Elements rows = row.getElementsByTag("tr");

            for (Element element : rows) {
                Elements link = element.select("td > a");
                if (link.size() == 1) {
                    String href = link.get(0).attr("href");
                    String name = link.get(0).html();

                    CardDetails cardDetails = cardService.getByName(name);
                    String cardId = "";
                    if (cardDetails != null) {
                        cardId = cardDetails.getId();
                    }
                    HearthpwnCardLink cardLink = new HearthpwnCardLink();
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
