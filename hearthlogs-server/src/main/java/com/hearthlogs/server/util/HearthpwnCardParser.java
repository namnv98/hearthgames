package com.hearthlogs.server.util;

import com.hearthlogs.server.service.CardService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class HearthPwnCardParser {

    private CardService cardService;
    private CardIdLinks cardIdLinks;

    private String HREF_REGEX = "/(.*?)-.*";
    private Pattern hearthPwnIdPattern = Pattern.compile(HREF_REGEX);

    @Autowired
    public HearthPwnCardParser(CardService cardService, CardIdLinks cardIdLinks) {
        this.cardService = cardService;
        this.cardIdLinks = cardIdLinks;
    }

    public List<HearthPwnCardLink> getUnknownCardLinks() throws IOException {

        List<HearthPwnCardLink> cardLinks = new ArrayList<>();
        for (int i=1; i < 16; i++) {
            String url = "http://www.hearthpwn.com/cards?display=1&sort=name&page=";

            Document doc = Jsoup.connect(url+i).userAgent("Mozilla").get();
            Element row = doc.getElementById("cards");
            Elements rows = row.getElementsByTag("tr");

            for (Element element : rows) {
                Elements link = element.select("td > a");
                if (link.size() == 1) {
                    String href = link.get(0).attr("href");
                    String name = link.get(0).html();

                    Matcher matcher = hearthPwnIdPattern.matcher(href);
                    String hearthPwnId = null;
                    if (matcher.matches()) {
                        hearthPwnId = matcher.group(1);
                    }
                    String cardId = null;
                    for (CardIdLink cardIdLink: cardIdLinks.getCards()) {
                        if (cardIdLink.getHearthPwnId().equals(hearthPwnId)) {
                            cardId = cardIdLink.getCardId();
                            break;
                        }
                    }

                    if (cardId == null) { // we don't about this card so we need to manually map it
                        List<String> cardNames = cardService.getCardIdsByName(name); // there might be multiple cards with the same name so we have to manually check after
                        HearthPwnCardLink cardLink = new HearthPwnCardLink();
                        cardLink.setCardId(StringUtils.join(cardNames, ","));
                        cardLink.setHref(href);
                        cardLink.setName(name);
                        cardLinks.add(cardLink);
                    }
                }
            }
        }
        return cardLinks;
    }

    public void downloadImages() throws IOException {

        for (int i=1; i < 16; i++) {
            String url = "http://www.hearthpwn.com/cards?display=3&page=";

            Document doc = Jsoup.connect(url+i).userAgent("Mozilla").get();
            Elements elements = doc.getElementsByClass("card-image-item");

            for (Element element: elements) {

                Elements link = element.select("a > .hscard-static");
                if (link.size() == 1) {


                    String src = link.get(0).attr("src");
                    String hearthPwnId = src.substring(src.lastIndexOf("/")+1);
                    hearthPwnId = hearthPwnId.substring(0, hearthPwnId.indexOf(".png"));

                    String cardId = null;
                    for (CardIdLink cardIdLink: cardIdLinks.getCards()) {
                        if (cardIdLink.getHearthPwnId().equals(hearthPwnId)) {
                            cardId = cardIdLink.getCardId();
                            break;
                        }
                    }

                    if (cardId == null) { // we only want to download cards we don't have
                        try {
                            FileUtils.copyURLToFile(new URL(src), new File("C:\\images\\download\\"+ hearthPwnId + ".png"));
                        } catch (Exception e) {
                            System.out.println("could not download: " + src);
                        }
                    }
                }
            }
        }
    }




}
