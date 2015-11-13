package com.hearthlogs.server.match.analysis;

import com.hearthlogs.server.match.analysis.domain.HealthArmorInfo;
import com.hearthlogs.server.match.analysis.domain.generic.GenericColumn;
import com.hearthlogs.server.match.analysis.domain.generic.GenericRow;
import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.parse.domain.Card;
import com.hearthlogs.server.match.play.MatchResult;
import com.hearthlogs.server.match.play.domain.Action;
import com.hearthlogs.server.match.play.domain.ArmorChange;
import com.hearthlogs.server.match.play.domain.HeroHealthChange;
import com.hearthlogs.server.match.play.domain.Turn;
import org.springframework.stereotype.Component;

@Component
public class HealthArmorInfoAnalyzer implements Analyzer<HealthArmorInfo> {

    @Override
    public HealthArmorInfo analyze(MatchResult result, ParseContext context) {
        HealthArmorInfo info = new HealthArmorInfo();

        GenericRow header = new GenericRow();
        info.setHeader(header);
        header.addColumn(new GenericColumn("Player"));
        for (int i=1; i <= result.getTurns().size(); i++) {
            header.addColumn(new GenericColumn(""+i));
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

        for (Turn turn: result.getTurns()) {
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
                col.setData2(""+friendlyArmor);
            }
            friendly.addColumn(col);

            col = new GenericColumn(""+opposingHealth);
            if (opposingArmor != 0) {
                col.setData2(""+opposingArmor);
            }
            opposing.addColumn(col);
        }

        String friendlyClass = result.getWinner() == result.getFriendly() ? result.getWinnerClass() : result.getLoserClass();
        String opposingClass = result.getWinner() == result.getOpposing() ? result.getWinnerClass() : result.getLoserClass();

        info.setFriendlyClass(friendlyClass);
        info.setOpposingClass(opposingClass);

        return info;
    }
}
