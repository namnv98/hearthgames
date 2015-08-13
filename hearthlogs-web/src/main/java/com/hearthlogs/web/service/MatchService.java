package com.hearthlogs.web.service;

import com.hearthlogs.web.domain.*;
import com.hearthlogs.web.log.parser.MatchActivityParser;
import com.hearthlogs.web.match.MatchContext;
import com.hearthlogs.web.match.RawMatch;
import com.hearthlogs.web.repository.solr.CompletedMatchRepository;
import com.hearthlogs.web.match.Activity;
import com.hearthlogs.web.match.CompletedMatch;
import com.hearthlogs.web.repository.jpa.RawMatchRepository;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.zip.InflaterInputStream;

@Component
public class MatchService {

    private static final Logger logger = LoggerFactory.getLogger(MatchService.class);

    private CardService cardService;
    private CompletedMatchRepository completedMatchRepository;
    private RawMatchRepository rawMatchRepository;
    private MatchActivityParser matchActivityParser;

    @Autowired
    public MatchService(CardService cardService,
                        MatchActivityParser matchActivityParser,
                        CompletedMatchRepository completedMatchRepository,
                        RawMatchRepository rawMatchRepository) {
        this.cardService = cardService;
        this.matchActivityParser = matchActivityParser;
        this.completedMatchRepository = completedMatchRepository;
        this.rawMatchRepository = rawMatchRepository;
    }

    public MatchContext deserializeGame(byte[] rawData) {
        MatchContext context = null;
        try {
            String game = decompressGameData(rawData);

            String[] lines = game.split("\n");

            context = new MatchContext();
            for (String line : lines) {
                matchActivityParser.parse(context, line);
            }

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logger.error(sw.toString());
        }
        return context;
    }

    public RawMatch saveRawPlayedGame(byte[] rawData, long startTime, long endTime, String rank) {
        RawMatch rawMatch = new RawMatch();
        rawMatch.setDateCreated(new Timestamp(System.currentTimeMillis()));
        rawMatch.setRawGame(rawData);
        rawMatch.setStartTime(new Timestamp(startTime));
        rawMatch.setEndTime(new Timestamp(endTime));
        rawMatch.setRank(rank);
        rawMatchRepository.save(rawMatch);
        return rawMatch;
    }

    public CompletedMatch processGame(MatchContext context, RawMatch rawMatch) {

        for (Activity activity: context.getActivities()) {
            handle(context, activity);
        }

        CompletedMatch completedMatch = context.getCompletedMatch();
        completedMatch.setId(rawMatch.getId()+"");
        completedMatch.setFriendlyPlayer(context.getFriendlyPlayer().getName());
        completedMatch.setOpposingPlayer(context.getOpposingPlayer().getName());
        completedMatch.setFriendlyAccountId(context.getFriendlyPlayer().getGameAccountIdLo());
        completedMatch.setOpposingAccountId(context.getOpposingPlayer().getGameAccountIdLo());
        completedMatch.setStartTime(rawMatch.getStartTime());
        completedMatch.setEndTime(rawMatch.getEndTime());
        completedMatch.setRank(rawMatch.getRank());

        Card card = (Card) context.getEntityById(context.getFriendlyPlayer().getHeroEntity());
        CardDetails cardDetails = cardService.getCardDetails(card.getCardid());
        completedMatch.setFriendlyClass(cardDetails.getPlayerClass());

        card = (Card) context.getEntityById(context.getOpposingPlayer().getHeroEntity());
        cardDetails = cardService.getCardDetails(card.getCardid());
        completedMatch.setOpposingClass(cardDetails.getPlayerClass());

        if ("1".equals(context.getFriendlyPlayer().getFirstPlayer())) {
            completedMatch.setFirstPlayer(context.getFriendlyPlayer().getName());
        } else {
            completedMatch.setFirstPlayer(context.getOpposingPlayer().getName());
        }

        return completedMatch;
    }

    public void indexGame(CompletedMatch completedMatch) {
        completedMatchRepository.save(completedMatch);
    }


    public void handle(MatchContext context, Activity activity) {
        // Handler the action first then any child actions. The reason for this is that the action contains a high level abstraction what is happening
        // For example:
        //    ACTION_START Entity=[name=Haunted Creeper id=75 zone=PLAY zonePos=1 cardId=FP1_002 player=2] SubType=ATTACK Index=-1 Target=[name=Jaina Proudmoore id=4 zone=PLAY zonePos=0 cardId=HERO_08 player=1]
        // means the Haunted Creeper minion is attacking Jaina Proudmoore
        // The details of the attack are in the child actions
        doActivity(context, activity);
        doChildActivities(context, activity.getChildren());
    }

    private void doChildActivities(MatchContext context, List<Activity> activities) {
        if (!CollectionUtils.isEmpty(activities)) {
            for (Activity activity : activities) {
                doChildActivities(context, activity.getChildren());
                doActivity(context, activity);
            }
        }
    }

    private void doActivity(MatchContext context, Activity activity) {
        playActivity(context, activity);
        updateGameActivityData(context, activity);
    }

    private void playActivity(MatchContext context, Activity activity) {
        handleGameUpdates(context, activity);
    }

    private void updateGameActivityData(MatchContext context, Activity activity) {
        if (activity.getType().equals(Activity.Type.TAG_CHANGE)) {
            copyNonNullProperties(activity.getEntity(), context.getEntityById(activity.getEntityId()));
        }
    }

    private void handleGameUpdates(MatchContext context, Activity activity) {
        if (activity.isAction()) {

            if (activity.getEntity() instanceof Card) {
                Card source = (Card) activity.getEntity();
                Card target = (Card) activity.getTarget();

                if (activity.isAttack()) {
                    System.out.println(getName(source.getCardid()) + " has attacked " + getName(target.getCardid()));
                }
                if (activity.isPower()) {
                    if (target == null) {
                        System.out.println(getName(source.getCardid()) + " has been cast");
                    } else {
                        System.out.println(getName(source.getCardid()) + " has been cast on " + getName(target.getCardid()));
                    }
                }

            }


        } else if (activity.isTagChange()) {

            if (activity.getEntity() instanceof Card) {
                Card before = (Card) context.getEntityById(activity.getEntityId());
                Card after = (Card) activity.getEntity();

                if ("HAND".equals(before.getZone()) && "DECK".equals(after.getZone())) {

                    Player player = getPlayer(context, before);
                    if (player.getEntityId().equals(context.getFriendlyPlayer().getEntityId())) {
                        context.getCompletedMatch().getFriendlyMulliganedCards().add(before.getCardid());
                        context.getCompletedMatch().getFriendlyStartingCards().remove(before.getCardid());
                    } else {
                        context.getCompletedMatch().getOpposingMulliganedCards().add(before.getCardid());
                        context.getCompletedMatch().getOpposingStartingCards().remove(before.getCardid());
                    }

                    System.out.println(getPlayer(context, before).getName() + " has mulliganed " + getName(before.getCardid()));
                }
                if ("DECK".equals(before.getZone()) && "HAND".equals(after.getZone())) {

                    Player player = getPlayer(context, before);

                    if ("BEGIN_MULLIGAN".equals(context.getGame().getStep())) {
                        if (player.getEntityId().equals(context.getFriendlyPlayer().getEntityId())) {
                            context.getCompletedMatch().getFriendlyStartingCards().add(before.getCardid());
                        } else {
                            context.getCompletedMatch().getOpposingStartingCards().add(before.getCardid());
                        }
                    }

                    if (player.getEntityId().equals(context.getFriendlyPlayer().getEntityId())) {
                        if (context.getStartingCardIds().contains(before.getEntityId())) {
                            context.getCompletedMatch().getFriendlyCards().add(getName(before.getCardid()));
                        }
                    } else {
                        if (context.getStartingCardIds().contains(before.getEntityId())) {
                            context.getCompletedMatch().getOpposingCards().add(getName(before.getCardid()));
                        }
                    }

                    System.out.println(getPlayer(context, before).getName() + " has drawn " + getName(before.getCardid()));
                }
                if ("HAND".equals(before.getZone()) && "PLAY".equals(after.getZone()) && "MINION".equals(before.getCardtype())) {
                    System.out.println(getPlayer(context, before).getName() + " has played " + getName(before.getCardid()));
                }
                if ("PLAY".equals(before.getZone()) && "GRAVEYARD".equals(after.getZone()) && "MINION".equals(before.getCardtype())) {
                    System.out.println(getName(before.getCardid()) + " has died.");
                }
            }

            if (activity.getEntity() instanceof Game) {
                Game before = (Game) context.getEntityById(activity.getEntityId());
                Game after = (Game) activity.getEntity();

                if (before.getStep() == null && "BEGIN_MULLIGAN".equals(after.getStep())) {
                    System.out.println("---------------------  The Game has started  ----------------------------------");
                    context.getCards().stream().filter(c -> "HAND".equals(c.getZone())).forEach(c -> {
                        System.out.println(getPlayer(context, c).getName() + " has drawn " + getName(c.getCardid()));
                    });


                    System.out.println();
                    System.out.println("--------------------  Mulligan Phase has started  -----------------------------");
                    System.out.println();
                }
                if ("BEGIN_MULLIGAN".equals(before.getStep()) && "MAIN_READY".equals(after.getStep())) {
                    System.out.println();
                    System.out.println("--------------------  Mulligan Phase has ended  -------------------------------");
                    System.out.println();
                }
                if ("MAIN_READY".equals(after.getStep())) {
                    Card friendlyHeroCard = (Card) context.getEntityById(context.getFriendlyPlayer().getHeroEntity());
                    Card opposingHeroCard = (Card) context.getEntityById(context.getOpposingPlayer().getHeroEntity());
                    printHeroHealth(context.getFriendlyPlayer(), friendlyHeroCard);
                    printHeroHealth(context.getOpposingPlayer(), opposingHeroCard);


                    context.getCompletedMatch().setTurns(Integer.valueOf(before.getTurn()));
                    System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  TURN " + before.getTurn() + "  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                }
                if ("COMPLETE".equals(after.getState())) {
                    Card friendlyHeroCard = (Card) context.getEntityById(context.getFriendlyPlayer().getHeroEntity());
                    Card opposingHeroCard = (Card) context.getEntityById(context.getOpposingPlayer().getHeroEntity());
                    printHeroHealth(context.getFriendlyPlayer(), friendlyHeroCard);
                    printHeroHealth(context.getOpposingPlayer(), opposingHeroCard);

                    System.out.println("--------------------------  Game Over  ----------------------------------------");
                }

            }


            if (activity.getEntity() instanceof Player) {
                Player before = (Player) context.getEntityById(activity.getEntityId());
                Player after = (Player) activity.getEntity();

                if (after.getPlaystate() != null && "QUIT".equals(after.getPlaystate())) {
                    System.out.println(getPlayer(context, before).getName() + " has quit.");
                    context.getCompletedMatch().setQuitter(getPlayer(context, before).getName());
                }
                if (after.getPlaystate() != null && "WON".equals(after.getPlaystate())) {
                    System.out.println(getPlayer(context, before).getName() + " has won.");
                    context.getCompletedMatch().setWinner(getPlayer(context, before).getName());
                }
                if (after.getPlaystate() != null && "LOST".equals(after.getPlaystate())) {
                    System.out.println(getPlayer(context, before).getName() + " has lost.");
                    context.getCompletedMatch().setLoser(getPlayer(context, before).getName());
                }
            }


//
//            if (hasValue("TURN_START", data)) {
//                System.out.println("The turn has started");
//            }
//


        } else { // we are starting an action, let's print out a summary of what the action is
//
//            POWER("POWER"),
//                    PLAY("PLAY"),
//                    TRIGGER("TRIGGER"),
//                    DEATHS("DEATHS"),
//                    ATTACK("ATTACK");

//            switch(activity.getSubType()) {
//                case POWER:
//
//            }


        }


    }

    private void printHeroHealth(Player player, Card heroCard) {
        int health = Integer.valueOf(heroCard.getHealth());
        int damage = heroCard.getDamage() != null ? Integer.valueOf(heroCard.getDamage()) : 0;

        System.out.println(player.getName() + " Hero Health = " + (health-damage));


    }

    private Player getPlayer(MatchContext context, Entity entity) {
        String id;
        if (entity instanceof Player) {
            id = ((Player) entity).getController();
        } else {
            id = ((Card) entity).getController();
        }
        if (context.getFriendlyPlayer().getController().equals(id)) return context.getFriendlyPlayer();
        return context.getOpposingPlayer();
    }

    private String getName(String id) {
        if ("".equals(id)) return "a card";
        String name = cardService.getName(id);
        return "".equals(name) ? "a card" : name;
    }

    public String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public void copyNonNullProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }


    public String decompressGameData(byte[] data) {
        try (
                InputStream is = new ByteArrayInputStream(data);
                InflaterInputStream iis = new InflaterInputStream(is);
                ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        ) {
            int b;
            while ((b = iis.read()) != -1) {
                baos.write(b);
            }
            return new String(baos.toByteArray(), "UTF-8");
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
