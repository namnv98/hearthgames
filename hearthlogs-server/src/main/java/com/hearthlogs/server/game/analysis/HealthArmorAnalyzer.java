package com.hearthlogs.server.game.analysis;

import com.hearthlogs.server.game.analysis.domain.generic.GenericTable;
import com.hearthlogs.server.game.play.domain.HeroHealthChange;
import com.hearthlogs.server.game.play.domain.Turn;
import com.hearthlogs.server.game.analysis.domain.generic.GenericColumn;
import com.hearthlogs.server.game.analysis.domain.generic.GenericRow;
import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.play.GameResult;
import com.hearthlogs.server.game.play.domain.Action;
import com.hearthlogs.server.game.play.domain.ArmorChange;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HealthArmorAnalyzer extends PagingAbstractAnalyzer<GenericTable> {

    protected GenericTable getInfo(GameResult result, GameContext context, List<Turn> turns) {
        GenericTable info = new GenericTable();

        GenericRow header = new GenericRow();
        info.setHeader(header);
        header.addColumn(new GenericColumn(""));
        for (Turn turn: turns) {
            header.addColumn(new GenericColumn(""+turn.getTurnNumber()));
        }

        GenericRow friendly = new GenericRow();
        info.setFriendly(friendly);
        friendly.addColumn(new GenericColumn(context.getFriendlyPlayer().getName()));

        GenericRow opposing = new GenericRow();
        info.setOpposing(opposing);
        opposing.addColumn(new GenericColumn(context.getOpposingPlayer().getName()));

        Card friendlyHeroCard = (Card) context.getEntityById(context.getFriendlyPlayer().getHeroEntity());
        Card opposingHeroCard = (Card) context.getEntityById(context.getOpposingPlayer().getHeroEntity());

        int friendlyHealth = Integer.parseInt(friendlyHeroCard.getHealth());
        int opposingHealth = Integer.parseInt(opposingHeroCard.getHealth());

        int friendlyArmor;
        try {
            friendlyArmor = Integer.parseInt(friendlyHeroCard.getArmor());
        } catch (NumberFormatException e) {
            friendlyArmor = 0;
        }
        int opposingArmor;
        try {
            opposingArmor = Integer.parseInt(opposingHeroCard.getArmor());
        } catch (NumberFormatException e) {
            opposingArmor = 0;
        }

        for (Turn turn: turns) {
            for (Action action: turn.getActions()) {
                if (action instanceof HeroHealthChange) {
                    HeroHealthChange change = (HeroHealthChange) action;
                    if (change.getCard().getController().equals(context.getFriendlyPlayer().getController())) {
                        friendlyHealth = change.getHealth();
                    } else {
                        opposingHealth = change.getHealth();
                    }
                } else if (action instanceof ArmorChange) {
                    ArmorChange change = (ArmorChange) action;
                    if (change.getCard().getController().equals(context.getFriendlyPlayer().getController())) {
                        friendlyArmor = change.getArmor();
                    } else {
                        opposingArmor = change.getArmor();
                    }
                }

            }
            GenericColumn col = new GenericColumn(""+friendlyHealth);
            if (friendlyArmor != 0) {
                col.setExtraData(""+friendlyArmor);
            }
            friendly.addColumn(col);

            col = new GenericColumn(""+opposingHealth);
            if (opposingArmor != 0) {
                col.setExtraData(""+opposingArmor);
            }
            opposing.addColumn(col);
        }

        return info;
    }
}
