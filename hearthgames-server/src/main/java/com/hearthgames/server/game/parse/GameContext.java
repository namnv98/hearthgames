package com.hearthgames.server.game.parse;

import com.hearthgames.server.game.parse.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.*;

public class GameContext {

    private static final Logger logger = LoggerFactory.getLogger(GameContext.class);

    private static final String GAME_ENTITY = "GameEntity";
    private static final String NAME = "name";
    private static final String TEAM_ID = "teamId";

    private static final String ID_EQUALS_ZERO = "0";

    private long activityId;
    private GameEntity gameEntity;
    private Player friendlyPlayer;
    private Player opposingPlayer;
    private Map<String, Card> cards = new HashMap<>();
    private List<String> startingCardIds = new ArrayList<>();

    private Deque<Activity> activityStack = new ArrayDeque<>();
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
    private String previousLine;
    private String currentLine;

    public void setGameEntity(GameEntity gameEntity) {
        this.gameEntity = gameEntity;
    }

    public void setFriendlyPlayer(Player friendlyPlayer) {
        this.friendlyPlayer = friendlyPlayer;
    }

    public void setOpposingPlayer(Player opposingPlayer) {
        this.opposingPlayer = opposingPlayer;
    }

    public Deque<Activity> getActivityStack() {
        return activityStack;
    }

    public Map<String, Card> getCards() {
        return cards;
    }

    public void addCard(Card card) {
        cards.put(card.getEntityId(), card);
    }

    public GameEntity getGameEntity() {
        return gameEntity;
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

    public Card getEntity(String entityStr) {
        Card entity;
        if (entityStr == null || ID_EQUALS_ZERO.equals(entityStr)) {
            return Card.UNKNOWN;
        }
        if (entityStr.equals(GAME_ENTITY) || gameEntity != null && entityStr.equals(gameEntity.getEntityId())) { // The match itself
            entity = gameEntity;
        } else if (friendlyPlayer.getName().equals(entityStr) || entityStr.equals(friendlyPlayer.getEntityId())) {
            entity = friendlyPlayer;
        } else if (opposingPlayer.getName().equals(entityStr) || entityStr.equals(opposingPlayer.getEntityId())) {
            entity = opposingPlayer;
        } else {
            entity = cards.get(entityStr);
        }
        return entity == null ? Card.UNKNOWN : entity;
    }

    public Card getEntityById(String id) {
        if (gameEntity.getEntityId().equals(id)) {
            return gameEntity;
        }
        if (friendlyPlayer.getEntityId().equals(id)) {
            return friendlyPlayer;
        }
        if (opposingPlayer.getEntityId().equals(id)) {
            return opposingPlayer;
        }
        return cards.get(id);
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

    public void setIndentLevel(String line) {
        this.previousLine = currentLine;
        this.previousIndentLevel = currentIndentLevel;
        this.currentIndentLevel = getIndentLevel(line);
        this.currentLine = line;
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

    public boolean isEndAction() {
        return currentIndentLevel < previousIndentLevel && previousLine != null && !previousLine.trim().startsWith("tag=");
    }

    public void populateEntity(Card entity, Map<String, String> data) {
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
        Activity activity = createActivity(dateTime, Activity.Type.TAG_CHANGE, player, getTempPlayerData());
        addActivity(activity);
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
        Activity activity = createActivity(dateTime, data);
        addActivity(activity);
        getActivityStack().push(activity);
    }

    public void createAction(LocalDateTime dateTime, String[] data) {
        processPendingCardUpdate(dateTime);
        Activity activity = createActivity(dateTime, data);
        addActivity(activity);
        getActivityStack().push(activity);
        setCreateAction(true);
    }

    public void endAction() {
        if (getActivityStack().isEmpty()) {
            return; // The log is missing data when spectating games.  So these games can't fully be serialized at the moment.
        }
        getActivityStack().pop();
    }

    public Activity createActivity(LocalDateTime dateTime, String[] data) {
        Card entity = getEntity(data[0]);
        Activity.BlockType blockType = Activity.BlockType.valueOf(data[1]);
        String index = data[2];
        Card target = getEntity(data[3]);
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
        String zone = card.getZone();
        if (!isGameUpdating()) {
            startingCardIds.add(card.getEntityId());
            if (!StringUtils.isEmpty(card.getCardid()) && !friendlyPlayer.getTeamId().equals(card.getController()) && !card.getCardtype().startsWith("HERO")) {
                Player swap = friendlyPlayer;
                friendlyPlayer = opposingPlayer;
                opposingPlayer = swap;
            }
        } else {
            card.setZone(Zone.DELAYED_PLAY.toString());
        }
        addCard(getCurrentCard());

        Activity activity = createActivity(dateTime, getCurrentCard());
        addActivity(activity);
        if (Zone.DELAYED_PLAY.eq(card.getZone())) {
            Map<String, String> data = new HashMap<>();
            data.put("zone", zone);
            activity = createActivity(dateTime, Activity.Type.TAG_CHANGE, card, data);
            addActivity(activity);
        }
        setCurrentCard(null);
        setCreateCard(false);
    }

    public void startCreateGame() {
        setGameEntity(new GameEntity());
        setCreateGameEntity(true);
    }

    public void endCreateGame(LocalDateTime dateTime) {
        setCreateGameEntity(false);
        Activity activity = createActivity(dateTime, getGameEntity());
        addActivity(activity);
    }

    public void updateCreateGame(Map<String, String> data) {
        populateEntity(getGameEntity(), data);
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
        addActivity(activity);
        setCurrentPlayer(null);
        setCreatePlayer(false);
    }

    public void tagChange(LocalDateTime dateTime, String entityStr, Map<String, String> data) {
        Card entity = getEntity(entityStr);
        Activity activity = createActivity(dateTime, Activity.Type.TAG_CHANGE, entity, data);
        addActivity(activity);
    }

    public void hideEntity(LocalDateTime dateTime, String entityStr, Map<String, String> data) {
        Card entity = getEntity(entityStr);
        Activity activity = createActivity(dateTime, Activity.Type.HIDE_ENTITY, entity, data);
        addActivity(activity);
    }

    public void updateCurrentCard(Map<String, String> data) {
        tempCardData.putAll(data);  // we want to bundle up all the tag updates to the card
    }

    public void endUpdateCard(LocalDateTime dateTime) {
        Activity activity = createActivity(dateTime, Activity.Type.SHOW_ENTITY, getCurrentCard(), tempCardData);
        addActivity(activity);
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
        Card c = cards.get(entityStr);
        if (c.getEntityId().equals(entityStr)) {
            c.setCardid(cardId);
            setCurrentCard(c);
            tempCardData = new HashMap<>();
        }
    }

    public void startUpdateGame(LocalDateTime dateTime, Map<String, String> data) {
        processPendingCardUpdate(dateTime);
        populateEntity(getGameEntity(), data);
        setGameUpdating(true);
    }

    public void endUpdateGame(LocalDateTime dateTime, Map<String, String> data) {
        Activity activity = createActivity(dateTime, Activity.Type.TAG_CHANGE, getGameEntity(), data);
        addActivity(activity);
        setGameUpdating(false);
    }

    public void updateCurrentGame(LocalDateTime dateTime, Map<String, String> data) {
        Activity activity = createActivity(dateTime, Activity.Type.TAG_CHANGE, getGameEntity(), data);
        addActivity(activity);
    }

    // This method is unfortunately required because an update card spans multiple lines and you can't tell when
    // updating is done until you start processing something else.
    private void processPendingCardUpdate(LocalDateTime dateTime) {
        if (isUpdateCard()) {
            endUpdateCard(dateTime);
        } else if (isCreateCard()) {
            endCreateCard(dateTime);
        }
    }

    public Activity createActivity(LocalDateTime dateTime, Card entity) {
        Activity activity = new Activity();
        activity.setId(activityId++);
        activity.setDateTime(dateTime);
        activity.setDelta(entity);
        activity.setEntityId(entity.getEntityId());
        if (entity.isGame()) {
            activity.setType(Activity.Type.NEW_GAME);
        } else if (entity.isPlayer()) {
            activity.setType(Activity.Type.NEW_PLAYER);
        } else {
            activity.setType(Activity.Type.NEW_CARD);
        }
        return activity;
    }

    public Activity createActivity(LocalDateTime dateTime, Activity.Type type, Card entity, Map<String, String> entityData) {
        Card data;
        if (entity.isGame()) {
            data = new GameEntity();
        } else if (entity.isPlayer()) {
            data = new Player();
        } else {
            data = new Card();
            data.setEntityId(entity.getEntityId());
            data.setCardid(entity.getCardid());
        }
        populateEntity(data, entityData);

        Activity activity = new Activity();
        activity.setDateTime(dateTime);
        activity.setId(activityId++);
        activity.setEntityId(entity.getEntityId());
        activity.setDelta(data);
        activity.setType(type);
        return activity;

    }

    public Activity createActivity(LocalDateTime dateTime, Card entity, Activity.BlockType blockType, String index, Card target) {
        Activity activity = new Activity();
        activity.setDateTime(dateTime);
        activity.setId(activityId++);
        activity.setEntityId(entity.getEntityId());
        activity.setDelta(entity);
        activity.setType(Activity.Type.ACTION);
        activity.setBlockType(blockType);
        activity.setIndex(index);
        activity.setTarget(target);
        return activity;
    }

    private void addActivity(Activity activity) {
        Activity currentActivity = null;
        if (!getActivityStack().isEmpty()) {
            currentActivity = getActivityStack().peek();
        }
        if (currentActivity == null) {
            getActivities().add(activity);
        } else {
            currentActivity.addChildGameEvent(activity);
        }

    }

    public List<String> getStartingCardIds() {
        return startingCardIds;
    }

    public Player getPlayerForCard(Card card) {
        return card.getController().equals(getFriendlyPlayer().getController()) ? getFriendlyPlayer() : getOpposingPlayer();
    }

    public Card getBefore(Activity activity) {
        return getEntityById(activity.getEntityId());
    }

    public Card getAfter(Activity activity) {
        return activity.getDelta();
    }

    public boolean isFriendly(Player player) {
        return player == getFriendlyPlayer();
    }

    public Player getPlayer(Card card) {
        return Objects.equals(card.getController(), getFriendlyPlayer().getController()) ? getFriendlyPlayer() : getOpposingPlayer();
    }

}