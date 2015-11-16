package com.hearthlogs.server.util;

import com.hearthlogs.server.match.parse.domain.CardDetails;
import com.hearthlogs.server.service.CardService;
import net.coobird.thumbnailator.Thumbnails;
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
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class HearthPwnCardParser {

    private CardService cardService;
    private HearthPwnCardLinks hearthPwnCardLinks;

    private String HREF_REGEX = "/cards/(.*?)-.*";
    private Pattern hearthPwnIdPattern = Pattern.compile(HREF_REGEX);

    @Autowired
    public HearthPwnCardParser(CardService cardService, HearthPwnCardLinks hearthPwnCardLinks) {
        this.cardService = cardService;
        this.hearthPwnCardLinks = hearthPwnCardLinks;
    }

    public List<HearthPwnCardLink> getUnknownCardLinks() throws IOException {

        List<HearthPwnCardLink> cardLinks = new ArrayList<>();
        for (int i=1; i < 16; i++) {
            String url = "http://www.hearthpwn.com/cards?display=2&page=";

            Document doc = Jsoup.connect(url+i).userAgent("Mozilla").get();
            Element row = doc.getElementById("cards");
            Elements rows = row.getElementsByTag("tr");

            for (Element element : rows) {
                Elements link = element.select("td.visual-image-cell > a");
                Elements nameEl = element.select("td.visual-details-cell > h3 > a");
                Elements textEl = element.select("td.visual-details-cell > p");
                if (link.size() == 1) {
                    String href = link.get(0).attr("href");
                    String name = nameEl.get(0).html();
                    String text = null;
                    if (textEl.size() > 0) {
                        text = textEl.get(0).html();
                    }

                    Matcher matcher = hearthPwnIdPattern.matcher(href);
                    String hearthPwnId = null;
                    if (matcher.matches()) {
                        hearthPwnId = matcher.group(1);
                    }
                    String cardId = null;
                    for (HearthPwnCardLink cardIdLink: hearthPwnCardLinks.getCards()) {
                        if (cardIdLink.getHearthPwnId().equals(hearthPwnId)) {
                            cardId = cardIdLink.getCardId();
                            break;
                        }
                    }

                    if (cardId == null) { // we don't about this card so we need to manually map it
                        List<CardDetails> cardDetailsList = cardService.getCardDetailsByName(name); // there might be multiple cards with the same name so we have to manually check after
                        HearthPwnCardLink cardLink = new HearthPwnCardLink();
                        final String finalText = text;
                        List<CardDetails> potentialCardDetails = cardDetailsList.stream().filter(cd -> Objects.equals(cd.getText(), finalText)).collect(Collectors.toList());
                        if (potentialCardDetails.isEmpty()) {
                            potentialCardDetails = cardDetailsList;
                        }
                        List<String> ids = potentialCardDetails.stream().map(CardDetails::getId).collect(Collectors.toList());
                        cardLink.setCardId(StringUtils.join(ids, ","));
                        cardLink.setHref(href);
                        cardLink.setName(name);
                        cardLink.setHearthPwnId(hearthPwnId);
                        cardLinks.add(cardLink);
                    }
                }
            }
        }
        cardLinks.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
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
                    if (StringUtils.isNotEmpty(src)) {
                        hearthPwnId = hearthPwnId.substring(0, hearthPwnId.indexOf(".png"));
                    }

                    String cardId = null;
                    for (HearthPwnCardLink cardIdLink: hearthPwnCardLinks.getCards()) {
                        if (cardIdLink.getHearthPwnId().equals(hearthPwnId)) {
                            cardId = cardIdLink.getCardId();
                            break;
                        }
                    }

                    if (cardId != null) { // we only want to download cards we don't have
                        try {
                            FileUtils.copyURLToFile(new URL(src), new File("C:\\images\\download\\"+ cardId+ ".png"));
                        } catch (Exception e) {
                            System.out.println("could not download: " + src);
                        }
                    } else {
                        System.out.println(src);
                    }
                }
            }
        }
    }


    public void makeThumbnails() throws IOException {

        for (HearthPwnCardLink cardIdLink: hearthPwnCardLinks.getCards()) {

            try {
                Thumbnails.of(new File("C:\\images\\download\\"+cardIdLink.getCardId()+".png"))
                        .size(40, 55)
                        .toFile(new File("C:\\images\\thumbs\\"+cardIdLink.getCardId()+".png"));
            } catch (Exception e) {
                System.out.println(cardIdLink.getCardId() + "," + cardIdLink.getHearthPwnId());
            }
        }
    }



}
