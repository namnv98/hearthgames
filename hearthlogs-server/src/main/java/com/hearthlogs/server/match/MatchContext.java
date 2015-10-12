package com.hearthlogs.server.match;

import com.hearthlogs.server.domain.Card;
import com.hearthlogs.server.domain.Entity;
import com.hearthlogs.server.domain.Game;
import com.hearthlogs.server.domain.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class MatchContext {

    private static final Logger logger = LoggerFactory.getLogger(MatchContext.class);

    private static final String GAME_ENTITY = "GameEntity";
    private static final String NAME = "name";
    private static final String TEAM_ID = "teamId";

    private long activityId;
    private Game game;
    private Player friendlyPlayer;
    private Player opposingPlayer;
    private List<Card> cards = new ArrayList<>();
    private List<String> startingCardIds = new ArrayList<>();

    private Stack<Activity> activityStack = new Stack<>();
    private List<Activity> activities = new ArrayList<>();

    private Card currentCard;
    private Player currentPlayer;
    private Map<String, String> tempPlayerData;
    private Map<String, String> tempCardData;
    private String currentPlayerName;

    private boolean createAction;
    private boolean createCard;
    private boolean createGameEntity;
    private boolean createPlayer;
    private boolean updateCard;
    private boolean gameRunning;
    private boolean updatePlayer;

    public void setGame(Game game) {
        this.game = game;
    }

    public void setFriendlyPlayer(Player friendlyPlayer) {
        this.friendlyPlayer = friendlyPlayer;
    }

    public void setOpposingPlayer(Player opposingPlayer) {
        this.opposingPlayer = opposingPlayer;
    }

    public Stack<Activity> getActivityStack() {
        return activityStack;
    }

    public List<Card> getCards() {
        return cards;
    }

    public Game getGame() {
        return game;
    }

    public Player getFriendlyPlayer() {
        return friendlyPlayer;
    }

    public Player getOpposingPlayer() {
        return opposingPlayer;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public Entity getEntity(String entityStr) {
        Entity entity = null;
        if (entityStr == null || "0".equals(entityStr)) return null;
        if (entityStr.equals(GAME_ENTITY)) { // The match itself
            entity = game;
        } else if (friendlyPlayer.getName().equals(entityStr)) {
            entity = friendlyPlayer;
        } else if (opposingPlayer.getName().equals(entityStr)) {
            entity = opposingPlayer;
        } else {
            for (Card c: cards) {
                if (c.getEntityId().equals(entityStr)) {
                    entity = c;
                    break;
                }
            }
        }
        return entity;
    }

    public Entity getEntityById(String id) {
        if (game.getEntityId().equals(id)) return game;
        if (friendlyPlayer.getEntityId().equals(id)) return friendlyPlayer;
        if (opposingPlayer.getEntityId().equals(id)) return opposingPlayer;
        for (Card c: cards) {
            if (c.getEntityId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public boolean isCreateAction() {
        return createAction;
    }

    public void setCreateAction(boolean createAction) {
        this.createAction = createAction;
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }

    public boolean isCreateCard() {
        return createCard;
    }

    public void setCreateCard(boolean createCard) {
        this.createCard = createCard;
    }

    public boolean isCreateGameEntity() {
        return createGameEntity;
    }

    public void setCreateGameEntity(boolean createGameEntity) {
        this.createGameEntity = createGameEntity;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public boolean isCreatePlayer() {
        return createPlayer;
    }

    public void setCreatePlayer(boolean createPlayer) {
        this.createPlayer = createPlayer;
    }

    public boolean isUpdateCard() {
        return updateCard;
    }

    public void setUpdateCard(boolean updateCard) {
        this.updateCard = updateCard;
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    public Map<String, String> getTempPlayerData() {
        return tempPlayerData;
    }

    public void setTempPlayerData(Map<String, String> tempPlayerData) {
        this.tempPlayerData = tempPlayerData;
    }

    public String getCurrentPlayerName() {
        return currentPlayerName;
    }

    public void setCurrentPlayerName(String currentPlayerName) {
        this.currentPlayerName = currentPlayerName;
    }

    public boolean isUpdatePlayer() {
        return updatePlayer;
    }

    public void setUpdatePlayer(boolean updatePlayer) {
        this.updatePlayer = updatePlayer;
    }


    public void populateEntity(Entity entity, Map<String, String> data) {
        for (Map.Entry<String, String> entry: data.entrySet()) {
            try {
                String key = entry.getKey();
                if (!Character.isDigit(key.charAt(0))) {
                    org.apache.commons.beanutils.BeanUtils.copyProperty(entity, key, entry.getValue());
                } else {
                    // not sure what the fields that start with digits are for but might as well record this data
                    entity.getUnknownTags().put(entry.getKey(), entry.getValue());
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.warn("Could not map tag : " + entry.getKey() + " for entity : " + entity.getClass().getName());
                // new tags may get added in the future, we don't want to miss this data
                entity.getUnknownTags().put(entry.getKey(), entry.getValue());
            }
        }
    }

    public void endUpdatePlayer() {
        Player player = null;
        if (getTempPlayerData() != null) {
            String teamId = getTempPlayerData().get(TEAM_ID);
            if (getFriendlyPlayer().getTeamId().equals(teamId)) {
                player = getFriendlyPlayer();
            } else if (getOpposingPlayer().getTeamId().equals(teamId)) {
                player = getOpposingPlayer();
            }
        }
        populateEntity(player, getTempPlayerData());
        Activity activity = createActivity(player, getTempPlayerData());
        getActivities().add(activity);
        setTempPlayerData(null);
        setUpdatePlayer(false);
    }

    public void startUpdatePlayer(String playerName, Map<String, String> data) {
        processPendingCardUpdate();
        setCurrentPlayerName(playerName);
        setTempPlayerData(new HashMap<>());
        getTempPlayerData().put(NAME, getCurrentPlayerName());
        updateCurrentPlayer(data);
        setUpdatePlayer(true);
    }

    public void updateCurrentPlayer(Map<String, String> data) {
        for (Map.Entry<String, String> entry: data.entrySet()) {
            getTempPlayerData().put(entry.getKey(), entry.getValue());
        }
    }

    public void createSubAction(String[] data) {
        getActivityStack().push(createActivity(data));
    }

    public void createAction(String data[]) {
        processPendingCardUpdate();
        getActivityStack().push(createActivity(data));
        setCreateAction(true);
    }

    public void endAction() {
        if (getActivityStack().isEmpty()) return; // The log is missing data when spectating games.  So these games can't fully be serialized at the moment.
        Activity activity = getActivityStack().pop();
        if (!getActivityStack().empty()) { // is there are more match events on the stack then we need to add this action as a subaction
            Activity parentActivity = getActivityStack().peek();
            parentActivity.addChildGameEvent(activity);
        } else { // no more actions so we are done with the action
            getActivities().add(activity);
            setCreateAction(!getActivityStack().empty());  // we are only out of the action when all sub actions are captured
        }
    }

    public Activity createActivity(String[] data) {
        Entity entity = getEntity(data[0]);
        Activity.BlockType blockType = Activity.BlockType.valueOf(data[1]);
        String index = data[2];
        Entity target = getEntity(data[3]);
        return createActivity(entity, blockType, index, target);
    }

    public void startCreateCard(Map<String, String> data) {
        processPendingCardUpdate();
        setCurrentCard(new Card());
        populateEntity(getCurrentCard(), data);
        setCreateCard(true);
    }

    public void updateCreateCard(Map<String, String> data) {
        populateEntity(getCurrentCard(), data);
    }

    public void endCreateCard() {
        Card card = getCurrentCard();
        if (!isGameRunning()) {
            startingCardIds.add(card.getEntityId());
            System.out.println("Adding " + card.getCardid() + " to starting cards");
            if (!StringUtils.isEmpty(card.getCardid()) && !friendlyPlayer.getTeamId().equals(card.getController()) && !card.getCardtype().startsWith("HERO")) {
                System.out.println("Found a known card that doesnt belong to friendly player...swapping players");
                Player swap = friendlyPlayer;
                friendlyPlayer = opposingPlayer;
                opposingPlayer = swap;
            }
        }
        getCards().add(getCurrentCard());
        Activity activity = createActivity(getCurrentCard());
        getActivities().add(activity);
        setCurrentCard(null);
        setCreateCard(false);
    }

    public void startCreateGame() {
        setGame(new Game());
        setCreateGameEntity(true);
    }

    public void endCreateGame() {
        setCreateGameEntity(false);
        Activity activity = createActivity(getGame());
        getActivities().add(activity);
    }

    public void updateCreateGame(Map<String, String> data) {
        populateEntity(getGame(), data);
    }

    public void startCreatePlayer(String[] data) {
        processPendingCardUpdate();
        setCurrentPlayer(new Player());
        getCurrentPlayer().setGameAccountIdHi(data[0]);
        getCurrentPlayer().setGameAccountIdLo(data[1]);
        setCreatePlayer(true);
    }

    public void updateCreatePlayer(Map<String, String> data) {
        populateEntity(getCurrentPlayer(), data);
    }

    public void endCreatePlayer() {
        String teamId = getCurrentPlayer().getTeamId();
        if ("1".equals(teamId)) {
            setFriendlyPlayer(getCurrentPlayer());
        } else if ("2".equals(teamId)) {
            setOpposingPlayer(getCurrentPlayer());
        }
        Activity activity = createActivity(getCurrentPlayer());
        getActivities().add(activity);
        setCurrentPlayer(null);
        setCreatePlayer(false);
    }

    public void tagChange(String entityStr, Map<String, String> data) {
        Activity currentActivity = null;
        if (!getActivityStack().empty()) {
            currentActivity = getActivityStack().peek();
        }
        Entity entity = getEntity(entityStr);
        if (entity != null) {
            Activity activity = createActivity(entity, data);
            if (currentActivity == null) {
                getActivities().add(activity);
            } else {
                currentActivity.addChildGameEvent(activity);
            }
        }
    }

    public void updateCurrentCard(Map<String, String> data) {
        tempCardData.putAll(data);  // we want to bundle up all the tag updates to the card
    }

    public void endUpdateCard() {
        Activity currentActivity = null;
        if (!getActivityStack().empty()) {
            currentActivity = getActivityStack().peek();
        }
        Activity activity = createActivity(getCurrentCard(), tempCardData);
        if (currentActivity == null) {  // a card update never seems to happen outside of an action but just in case in the future it does we will leave this code here
            getActivities().add(activity);
        } else {
            currentActivity.addChildGameEvent(activity);
        }
        tempCardData = null;
        setCurrentCard(null);
        setUpdateCard(false);
    }

    public void startUpdateCard(String entityStr, String cardId) {
        processPendingCardUpdate();
        setUpdateCard(true);
        // In all other cases the change of state of an entity is saved in an match activity but this is just a CARDID update
        // so having this data before hand during a replay of the match opens up extra information that was revealed later in the match
        // (example: we know what IDs of cards an opponent drew and mulliganed but later in the match we see those cards drawn again and revealed)
        for (Card c: getCards()) {
            if (c.getEntityId().equals(entityStr)) {
                c.setCardid(cardId);
                setCurrentCard(c);
                tempCardData = new HashMap<>();
                break;
            }
        }
    }

    public void startUpdateGame(Map<String, String> data) {
        processPendingCardUpdate();
        populateEntity(getGame(), data);
        setGameRunning(true);
    }

    public void endUpdateGame(Map<String, String> data) {
        Activity activity = createActivity(getGame(), data);
        getActivities().add(activity);
        setGameRunning(false);
    }

    public void updateCurrentGame(Map<String, String> data) {
        Activity activity = createActivity(getGame(), data);
        getActivities().add(activity);
    }

    // This method is unfortunately required because an update card spans multiple lines and you can't tell when
    // updating is done until you start processing something else.
    private void processPendingCardUpdate() {
        if (isUpdateCard()) {
            endUpdateCard();
        }
    }

    public Activity createActivity(Entity entity) {
        Activity activity = new Activity();
        activity.setEntity(entity);
        if (entity instanceof Card) {
            activity.setType(Activity.Type.NEW_CARD);
        } else if (entity instanceof Game) {
            activity.setType(Activity.Type.NEW_GAME);
        } else if (entity instanceof Player) {
            activity.setType(Activity.Type.NEW_PLAYER);
        }
        return activity;
    }

    public Activity createActivity(Entity entity, Map<String, String> entityData) {
        Entity data = null;
        if (entity instanceof Card) {
            data = new Card();
        } else if (entity instanceof Game) {
            data = new Game();
        } else if (entity instanceof Player) {
            data = new Player();
        }
        populateEntity(data, entityData);

        Activity activity = new Activity();
        activity.setId(activityId++);
        activity.setEntityId(entity.getEntityId());
        activity.setEntity(data);
        activity.setType(Activity.Type.TAG_CHANGE);
        return activity;

    }

    public Activity createActivity(Entity entity, Activity.BlockType blockType, String index, Entity target) {
        Activity activity = new Activity();
        activity.setId(activityId++);
        activity.setEntityId(entity.getEntityId());
        activity.setEntity(entity);
        activity.setType(Activity.Type.ACTION);
        activity.setBlockType(blockType);
        activity.setIndex(index);
        activity.setTarget(target);
        return activity;
    }

    public List<String> getStartingCardIds() {
        return startingCardIds;
    }

    private Player winner;
    private Player loser;
    private Player quitter;
    private Player first;

    private List<Card> friendlyStartingCards = new ArrayList<>();
    private List<Card> opposingStartingCards = new ArrayList<>();

    private List<Card> friendlyMulliganedCards = new ArrayList<>();
    private List<Card> opposingMulliganedCards = new ArrayList<>();

    private List<Card> friendlyCards = new ArrayList<>();  // the list of cards that were part of the deck.  This will not include cards created by other cards
    private List<Card> opposingCards = new ArrayList<>();  // the list of cards that were part of the deck.  This will not include cards created by other cards

    private List<Card> friendlyCreatedCards = new ArrayList<>();
    private List<Card> opposingCreatedCards = new ArrayList<>();

    private String turns;

    public void addFriendlyStartingCard(Card card) {
        friendlyStartingCards.add(card);
    }

    public void addOpposingStartingCard(Card card) {
        opposingStartingCards.add(card);
    }

    public void addFriendlyCard(Card card) {
        friendlyCards.add(card);
    }

    public void addOpposingCard(Card card) {
        opposingCards.add(card);
    }

    public void mulliganFriendlyCard(Card card) {
        friendlyMulliganedCards.add(card);
    }

    public void mulliganOpposingCard(Card card) {
        opposingMulliganedCards.add(card);
    }

    public void addFriendlyCreatedCard(Card card) {
        friendlyCreatedCards.add(card);
    }

    public void addOpposingCreatedCard(Card card) {
        opposingCreatedCards.add(card);
    }

    public List<Card> getFriendlyStartingCards() {
        return friendlyStartingCards;
    }

    public List<Card> getOpposingStartingCards() {
        return opposingStartingCards;
    }

    public List<Card> getFriendlyMulliganedCards() {
        return friendlyMulliganedCards;
    }

    public List<Card> getOpposingMulliganedCards() {
        return opposingMulliganedCards;
    }

    public List<Card> getFriendlyCards() {
        return friendlyCards;
    }

    public List<Card> getOpposingCards() {
        return opposingCards;
    }

    public List<Card> getOpposingCreatedCards() {
        return opposingCreatedCards;
    }

    public List<Card> getFriendlyCreatedCards() {
        return friendlyCreatedCards;
    }

    public String getTurns() {
        return turns;
    }

    public void setTurns(String turns) {
        this.turns = turns;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public Player getLoser() {
        return loser;
    }

    public void setLoser(Player loser) {
        this.loser = loser;
    }

    public Player getQuitter() {
        return quitter;
    }

    public void setQuitter(Player quitter) {
        this.quitter = quitter;
    }

    public Player getFirst() {
        return first;
    }

    public void setFirst(Player first) {
        this.first = first;
    }
}