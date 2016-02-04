package com.hearthgames.utils.hearthpwn;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class DeckUtil {

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException, TimeoutException {

        ExecutorService executor = Executors.newFixedThreadPool(8);

        List<Future<?>> futures = new ArrayList<>();

        for (int j=70000; j <= 360000; j = j + 10000) {
            final int finalJ = j;
            futures.add(executor.submit(() -> {
                String url = "http://www.hearthpwn.com/decks/";

                int end = finalJ+10000;

                for (int i = finalJ; i <= end; i++) {

                    try {
                        Document doc = Jsoup.connect(url+i+"/export/1").userAgent("Mozilla").get();
                        Elements rows = doc.getElementsByClass("deck-export-area");
                        if (rows.size() == 1) {
                            Element element = rows.get(0);
                            FileUtils.writeStringToFile(new File("c:\\decks\\"+i+".deck"), element.val());
                        }
                    } catch (Exception e) {
                    }
                }
            }));
        }

        for (Future<?> future: futures) {
            future.get(1, TimeUnit.SECONDS);
        }






    }

}
