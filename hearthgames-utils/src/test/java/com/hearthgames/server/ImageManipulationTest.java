package com.hearthgames.server;

import com.hearthgames.utils.UtilsApplication;
import com.hearthgames.utils.hearthpwn.CardDetails;
import com.hearthgames.utils.hearthpwn.CardService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = UtilsApplication.class)
public class ImageManipulationTest {

    @Autowired
    CardService cardService;


    @Test
    public void shouldMakeHeroes() throws IOException {

        File dir = new File("C:\\images\\heroes");
        File[] files = dir.listFiles();

        List<String> heroes = new ArrayList<>();
        if (files != null) {
            for (File file: files) {
                String name = file.getName().replace(".png","");
                heroes.add(name);
            }

        }
        for (String hero: heroes) {

            System.out.println("."+hero+"_h { background-image: url(\"http://images.hearthgames.com/h/"+hero+".png\"); }");


        }

        System.out.println();

    }


    @Test
    public void shouldMakeMinions() throws IOException {

        Collection<CardDetails> cards = cardService.getCardDetails();

        List<CardDetails> minions = cards.stream().filter(card -> "MINION".equals(card.getType())).collect(Collectors.toList());

        File dir = new File("C:\\images\\minons");
        File[] files = dir.listFiles();

        List<String> cardFiles = new ArrayList<>();
        if (files != null) {
            for (File file: files) {
                String name = file.getName().replace(".png","");
                cardFiles.add(name);
            }

        }
        for (CardDetails minion: minions) {
            if (!cardFiles.contains(minion.getId())) {
                System.out.println(minion.getName()+","+minion.getId());
            }
        }

        System.out.println();

    }

    @Test
    public void shouldMakeHeroPowers() throws IOException {

        Collection<CardDetails> cards = cardService.getCardDetails();

        List<CardDetails> heroPowers = cards.stream().filter(card -> "HERO_POWER".equals(card.getType())).collect(Collectors.toList());

        File dir = new File("C:\\images\\download");
        File[] files = dir.listFiles();

        List<String> cardFiles = new ArrayList<>();
        if (files != null) {
            for (File file: files) {
                String name = file.getName().replace(".png","");
                cardFiles.add(name);
            }

        }
        for (CardDetails heroPower: heroPowers) {
            if (cardFiles.contains(heroPower.getId())) {
                File file = new File("c:\\images\\download\\"+heroPower.getId()+".png");
                if (file.exists()) {
//                    FileUtils.copyFile(file, new File("c:\\images\\heropowers\\"+heroPower.getId()+".png"));
                    System.out.println(".heropower ."+heroPower.getId() + " { background-image: url(\"http://images.hearthgames.com/hp/"+heroPower.getId() +".png\"); }");
                }
//                else {
//                    System.out.println(heroPower.getId()+" does not exist");
//                }
            }
        }
        System.out.println();
    }


    @Test
    public void shouldMakeWeapons() throws IOException {

        Collection<CardDetails> cards = cardService.getCardDetails();

        List<CardDetails> weapons = cards.stream().filter(card -> "WEAPON".equals(card.getType())).collect(Collectors.toList());

        File dir = new File("C:\\images\\download");
        File[] files = dir.listFiles();

        List<String> cardFiles = new ArrayList<>();
        if (files != null) {
            for (File file: files) {
                String name = file.getName().replace(".png","");
                cardFiles.add(name);
            }

        }
        for (CardDetails weapon: weapons) {
//            if (cardFiles.contains(weapon.getId())) {
                File file = new File("c:\\images\\weaponoutput\\"+weapon.getId()+".png");
                if (!file.exists()) {
//                    FileUtils.copyFile(file, new File("c:\\images\\weapons\\"+weapon.getId()+".png"));
                    System.out.println(".weapon ."+weapon.getId() + " { background-image: url(\"http://images.hearthgames.com/w/"+weapon.getId() +".png\"); }");
                }
//                else {
//                    System.out.println(heroPower.getId()+" does not exist");
//                }
//            }
        }
        System.out.println();
    }


}