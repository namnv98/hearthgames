package com.hearthgames.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hearthgames.utils.hearthpwn.CardCollection;
import com.hearthgames.utils.hearthpwn.CardDetails;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogUtil {

    public static void main(String[] args) throws Exception {

        CardCollection cardCollection = new ObjectMapper().readValue(LogUtil.class.getClassLoader().getResourceAsStream("AllSets.json"), CardCollection.class);

        Map<String, List<CardDetails>> playableCards = cardCollection.getPlayableCards();

        Collection<File> files = FileUtils.listFiles(new File("C:\\games"), new String[]{"txt"}, false);

        Pattern pattern = Pattern.compile(".*unloading name=.*/(.*?)/(.*?) family.*");

        Map<String, Set<String>> imageNameMapping = new HashMap<>();

        Set<String> cardIds = new LinkedHashSet<>();

        for (File file: files) {
            List<String> lines = FileUtils.readLines(file);
            for (String line: lines) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    String cardId = matcher.group(1);
                    String fileName = matcher.group(2);

                    Set<String> fileNames = imageNameMapping.get(cardId);
                    if (imageNameMapping.get(cardId) == null) {
                        fileNames = new LinkedHashSet<>();
                    }
                    fileNames.add(fileName);
                    imageNameMapping.put(cardId, fileNames);

                    cardIds.add(cardId);
                }
            }
        }

        for (String card: cardIds) {
            removeCard(playableCards, card);
        }


        System.out.println();

    }


    private static void removeCard(Map<String, List<CardDetails>> playableCards, String cardId) {
        for (String set: playableCards.keySet()) {
            List<CardDetails> cards = playableCards.get(set);
            Iterator<CardDetails> cardDetailsIterator = cards.iterator();
            while (cardDetailsIterator.hasNext()) {
                CardDetails cardDetails = cardDetailsIterator.next();
                if (cardDetails.getId().equals(cardId)) {
                    cardDetailsIterator.remove();
                } else if ("ENCHANTMENT".equals(cardDetails.getType())) {
                    cardDetailsIterator.remove();
                }
            }
        }
    }

}
