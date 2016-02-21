package com.hearthgames.server.util;

import com.hearthgames.server.game.parse.GameState;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.domain.*;
import com.hearthgames.server.game.play.domain.board.Board;

import java.util.List;

public class HeroStatsUtil {


    public static Integer getHealth(Player player, List<Action> actions, Board boardToStopAt) {
        return doGetHeroHealth(player, actions, boardToStopAt);
    }

    public static Integer getHealth(Player player, List<Action> actions) {
        return doGetHeroHealth(player, actions, null);
    }

    public static Integer getArmor(Player player, List<Action> actions, Board boardToStopAt) {
        return doGetHeroArmor(player, actions, boardToStopAt);
    }

    public static Integer getArmor(Player player, List<Action> actions) {
        return doGetHeroArmor(player, actions, null);
    }

    public static boolean hasHealthChanged(Player player, List<Action> actions, Board boardToStopAt) {
        return doHasHealthChanged(player, actions, boardToStopAt);
    }

    public static boolean hasHealthChanged(Player player, List<Action> actions) {
        return doHasHealthChanged(player, actions, null);
    }

    public static boolean hasArmorChanged(Player player, List<Action> actions, Board boardToStopAt) {
        return doHasArmorChanged(player, actions, boardToStopAt);
    }

    public static boolean hasArmorChanged(Player player, List<Action> actions) {
        return doHasArmorChanged(player, actions, null);
    }

    public static Card getHeroCard(Player player, GameState gameState) {
        return gameState.getEntityById(player.getHeroEntity());
    }

    public static Integer getCurrentHealth(Player player, GameState gameState) {
        Card heroCard = gameState.getEntityById(player.getHeroEntity());
        return heroCard.getHealth() != null ? Integer.parseInt(heroCard.getHealth()) : 0;
    }

    public static Integer getCurrentArmor(Player player, GameState gameState) {
        Card heroCard = gameState.getEntityById(player.getHeroEntity());
        Integer armor;
        try {
            armor = Integer.parseInt(heroCard.getArmor());
        } catch (NumberFormatException e) {
            armor = null;
        }
        return armor;
    }


    public static Integer getManaUsed(Player player, Turn turn, Action stopAction) {
        Integer manaUsed = 0;
        if (turn.getWhoseTurn() == player) {
            for (Action action: turn.getActions()) {
                if (stopAction != null && stopAction == action) {
                    break;
                }
                if (action instanceof ManaUsed) {
                    manaUsed += ((ManaUsed) action).getAmount();
                }
            }
        }
        return manaUsed;
    }

    public static Integer getManaGained(Player player, Turn turn, Action stopAction) {
        Integer manaGained = 0;
        if (turn.getWhoseTurn() == player) {
            for (Action action: turn.getActions()) {
                if (stopAction != null && stopAction == action) {
                    break;
                }
                if (action instanceof ManaGained) {
                    manaGained += ((ManaGained) action).getAmount();
                } else if (action instanceof TempManaGained) {
                    manaGained += ((TempManaGained) action).getAmount();
                }
            }
        }
        return manaGained;
    }

    private static boolean doHasHealthChanged(Player player, List<Action> actions, Action stopAction) {
        boolean changed = false;
        for (Action action: actions) {
            if (stopAction != null && stopAction == action) {
                break;
            }
            if (action instanceof HeroHealthChange) {
                HeroHealthChange change = (HeroHealthChange) action;
                if (change.getCard().getController().equals(player.getController())) {
                    changed = true;
                }
            }
        }
        return changed;
    }

    private static boolean doHasArmorChanged(Player player, List<Action> actions, Action stopAction) {
        boolean changed = false;
        for (Action action: actions) {
            if (stopAction != null && stopAction == action) {
                break;
            }
            if (action instanceof ArmorChange) {
                ArmorChange change = (ArmorChange) action;
                if (change.getCard().getController().equals(player.getController())) {
                    changed = true;
                }
            }
        }
        return changed;
    }

    private static Integer doGetHeroHealth(Player player, List<Action> actions, Action stopAction) {
        Integer health = null;
        for (Action action: actions) {
            if (stopAction != null && stopAction == action) {
                break;
            }
            if (action instanceof HeroHealthChange) {
                HeroHealthChange change = (HeroHealthChange) action;
                if (change.getCard().getController().equals(player.getController())) {
                    health = change.getHealth();
                }
            }
        }
        return health;
    }

    private static Integer doGetHeroArmor(Player player, List<Action> actions, Action stopAction) {
        Integer armor = null;
        for (Action action: actions) {
            if (stopAction != null && stopAction == action) {
                break;
            }
            if (action instanceof ArmorChange) {
                ArmorChange change = (ArmorChange) action;
                if (change.getCard().getController().equals(player.getController())) {
                    armor = change.getArmor();
                }
            }        }
        return armor;
    }



}
