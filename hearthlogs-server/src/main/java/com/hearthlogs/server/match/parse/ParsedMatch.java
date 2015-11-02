package com.hearthlogs.server.match.parse;

import com.hearthlogs.server.match.parse.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.*;

public class ParsedMatch {

    private static final Logger logger = LoggerFactory.getLogger(ParsedMatch.class);

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
    private boolean gameUpdating;
    private boolean updatePlayer;

    private int currentIndentLevel;
    private int previousIndentLevel;

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

    public boolean isGameUpdating() {
        return gameUpdating;
    }

    public void setGameUpdating(boolean gameUpdating) {
        this.gameUpdating = gameUpdating;
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

    public int getCurrentIndentLevel() {
        return currentIndentLevel;
    }

    public void setCurrentIndentLevel(int currentIndentLevel) {
        this.currentIndentLevel = currentIndentLevel;
    }

    public int getPreviousIndentLevel() {
        return previousIndentLevel;
    }

    public void setPreviousIndentLevel(int previousIndentLevel) {
        this.previousIndentLevel = previousIndentLevel;
    }

    public void setIndentLevel(String line) {
        this.previousIndentLevel = currentIndentLevel;
        this.currentIndentLevel = getIndentLevel(line);
    }

    private int getIndentLevel(String line) {
        int count = 0;
        for (char c: line.toCharArray()) {
            if (Character.isWhitespace(c)) {
                count++;
            } else {
                break;
            }
        }
        return count > 0 ? count / 4 : 0;
    }

    public boolean hasIndentationDecreased() {
        return currentIndentLevel < previousIndentLevel;
    }

    public boolean hasIndentationIncreased() {
        return currentIndentLevel > previousIndentLevel;
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

    public void endUpdatePlayer(LocalDateTime dateTime) {
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
        Activity activity = createActivity(dateTime, Activity.Type.TAG_CHANGE ,player, getTempPlayerData());
        getActivities().add(activity);
        setTempPlayerData(null);
        setUpdatePlayer(false);
    }

    public void startUpdatePlayer(LocalDateTime dateTime, String playerName, Map<String, String> data) {
        processPendingCardUpdate(dateTime);
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

    public void createSubAction(LocalDateTime dateTime, String[] data) {
        getActivityStack().push(createActivity(dateTime, data));
    }

    public void createAction(LocalDateTime dateTime, String data[]) {
        processPendingCardUpdate(dateTime);
        getActivityStack().push(createActivity(dateTime, data));
        setCreateAction(true);
    }

    public void endAction() {
        if (getActivityStack().isEmpty()) return; // The log is missing data when spectating games.  So these games can't fully be serialized at the moment.
        Activity activity = getActivityStack().pop();
        if (!getActivityStack().empty()) { // is there are more match events on the stack then we need to add this domain as a subaction
            Activity parentActivity = getActivityStack().peek();
            parentActivity.addChildGameEvent(activity);
        } else { // no more actions so we are done with the domain
            getActivities().add(activity);
            setCreateAction(!getActivityStack().empty());  // we are only out of the domain when all sub actions are captured
        }
    }

    public Activity createActivity(LocalDateTime dateTime, String[]data) {
        Entity entity = getEntity(data[0]);
        Activity.BlockType blockType = Activity.BlockType.valueOf(data[1]);
        String index = data[2];
        Entity target = getEntity(data[3]);
        return createActivity(dateTime, entity, blockType, index, target);
    }

    public void startCreateCard(LocalDateTime dateTime, Map<String, String> data) {
        processPendingCardUpdate(dateTime);
        setCurrentCard(new Card());
        populateEntity(getCurrentCard(), data);
        setCreateCard(true);
    }

    public void updateCreateCard(Map<String, String> data) {
        populateEntity(getCurrentCard(), data);
    }

    public void endCreateCard(LocalDateTime dateTime) {
        Card card = getCurrentCard();
        if (!isGameUpdating()) {
            startingCardIds.add(card.getEntityId());
//            System.out.println("Adding " + card.getCardid() + " to starting cards (id=" + card.getEntityId()+ ")");
            if (!StringUtils.isEmpty(card.getCardid()) && !friendlyPlayer.getTeamId().equals(card.getController()) && !card.getCardtype().startsWith("HERO")) {
//                System.out.println("Found a known card that doesnt belong to friendly player...swapping players");
                Player swap = friendlyPlayer;
                friendlyPlayer = opposingPlayer;
                opposingPlayer = swap;
            }
        }
        getCards().add(getCurrentCard());

        Activity currentActivity = null;
        if (!getActivityStack().empty()) {
            currentActivity = getActivityStack().peek();
        }
        Activity activity = createActivity(dateTime, getCurrentCard());
        if (currentActivity == null) {
            getActivities().add(activity);
        } else {
            currentActivity.addChildGameEvent(activity);
        }
        setCurrentCard(null);
        setCreateCard(false);
    }

    public void startCreateGame() {
        setGame(new Game());
        setCreateGameEntity(true);
    }

    public void endCreateGame(LocalDateTime dateTime) {
        setCreateGameEntity(false);
        Activity activity = createActivity(dateTime, getGame());
        getActivities().add(activity);
    }

    public void updateCreateGame(Map<String, String> data) {
        populateEntity(getGame(), data);
    }

    public void startCreatePlayer(LocalDateTime dateTime, String[] data) {
        processPendingCardUpdate(dateTime);
        setCurrentPlayer(new Player());
        getCurrentPlayer().setGameAccountIdHi(data[0]);
        getCurrentPlayer().setGameAccountIdLo(data[1]);
        setCreatePlayer(true);
    }

    public void updateCreatePlayer(Map<String, String> data) {
        populateEntity(getCurrentPlayer(), data);
    }

    public void endCreatePlayer(LocalDateTime dateTime) {
        String teamId = getCurrentPlayer().getTeamId();
        if ("1".equals(teamId)) {
            setFriendlyPlayer(getCurrentPlayer());
        } else if ("2".equals(teamId)) {
            setOpposingPlayer(getCurrentPlayer());
        }
        Activity activity = createActivity(dateTime, getCurrentPlayer());
        getActivities().add(activity);
        setCurrentPlayer(null);
        setCreatePlayer(false);
    }

    public void tagChange(LocalDateTime dateTime, String entityStr, Map<String, String> data) {
        Activity currentActivity = null;
        if (!getActivityStack().empty()) {
            currentActivity = getActivityStack().peek();
        }
        Entity entity = getEntity(entityStr);
        if (entity != null) {
            Activity activity = createActivity(dateTime, Activity.Type.TAG_CHANGE, entity, data);
            if (currentActivity == null) {
                getActivities().add(activity);
            } else {
                currentActivity.addChildGameEvent(activity);
            }
        }
    }

    public void hideEntity(LocalDateTime dateTime, String entityStr, Map<String, String> data) {
        Activity currentActivity = null;
        if (!getActivityStack().empty()) {
            currentActivity = getActivityStack().peek();
        }
        Entity entity = getEntity(entityStr);
        if (entity != null) {
            Activity activity = createActivity(dateTime, Activity.Type.HIDE_ENTITY, entity, data);
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

    public void endUpdateCard(LocalDateTime dateTime) {
        Activity currentActivity = null;
        if (!getActivityStack().empty()) {
            currentActivity = getActivityStack().peek();
        }
        Activity activity = createActivity(dateTime, Activity.Type.SHOW_ENTITY, getCurrentCard(), tempCardData);
        if (currentActivity == null) {  // a card update never seems to happen outside of an domain but just in case in the future it does we will leave this code here
            getActivities().add(activity);
        } else {
            currentActivity.addChildGameEvent(activity);
        }
        tempCardData = null;
        setCurrentCard(null);
        setUpdateCard(false);
    }

    public void startUpdateCard(LocalDateTime dateTime, String entityStr, String cardId) {
        processPendingCardUpdate(dateTime);
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

    public void startUpdateGame(LocalDateTime dateTime, Map<String, String> data) {
        processPendingCardUpdate(dateTime);
        populateEntity(getGame(), data);
        setGameUpdating(true);
    }

    public void endUpdateGame(LocalDateTime dateTime, Map<String, String> data) {
        Activity activity = createActivity(dateTime, Activity.Type.TAG_CHANGE, getGame(), data);
        getActivities().add(activity);
        setGameUpdating(false);
    }

    public void updateCurrentGame(LocalDateTime dateTime, Map<String, String> data) {
        Activity activity = createActivity(dateTime, Activity.Type.TAG_CHANGE, getGame(), data);
        getActivities().add(activity);
    }

    // This method is unfortunately required because an update card spans multiple lines and you can't tell when
    // updating is done until you start processing something else.
    private void processPendingCardUpdate(LocalDateTime dateTime) {
        if (isUpdateCard()) {
            endUpdateCard(dateTime);
        }
    }

    public Activity createActivity(LocalDateTime dateTime, Entity entity) {
        Activity activity = new Activity();
        activity.setDateTime(dateTime);
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

    public Activity createActivity(LocalDateTime dateTime, Activity.Type type, Entity entity, Map<String, String> entityData) {
        Entity data = null;
        if (entity instanceof Card) {
            data = new Card();
            data.setEntityId(entity.getEntityId());
            ((Card) data).setCardid(((Card) entity).getCardid());
        } else if (entity instanceof Game) {
            data = new Game();
        } else if (entity instanceof Player) {
            data = new Player();
        }
        populateEntity(data, entityData);

        Activity activity = new Activity();
        activity.setDateTime(dateTime);
        activity.setId(activityId++);
        activity.setEntityId(entity.getEntityId());
        activity.setEntity(data);
        activity.setType(type);
        return activity;

    }

    public Activity createActivity(LocalDateTime dateTime, Entity entity, Activity.BlockType blockType, String index, Entity target) {
        Activity activity = new Activity();
        activity.setDateTime(dateTime);
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

    public boolean isMatchValid() {
        // We found a match that was played against the computer.  This is not acceptable.
        return !"0".equals(getOpposingPlayer().getGameAccountIdLo());
    }

}