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

import java.util.ArrayList;
import java.util.List;

@Component
public class HealthArmorInfoAnalyzer implements Analyzer<List<HealthArmorInfo>> {

    @Override
    public List<HealthArmorInfo> analyze(MatchResult result, ParseContext context) {

        List<HealthArmorInfo> infos = new ArrayList<>();

        List<Turn> subSetOfTurns = new ArrayList<>();
        for (Turn turn: result.getTurns()) {
            if (turn.getTurnNumber() % 24 == 0) {
                subSetOfTurns.add(turn);
                HealthArmorInfo info = getHealthArmorInfo(result, context, subSetOfTurns);
                infos.add(info);
                subSetOfTurns = new ArrayList<>();
            } else {
                subSetOfTurns.add(turn);
            }
        }
        if (subSetOfTurns.size() > 0) {
            HealthArmorInfo info = getHealthArmorInfo(result, context, subSetOfTurns);
            infos.add(info);
        }
        return infos;
    }

    private HealthArmorInfo getHealthArmorInfo(MatchResult result, ParseContext context, List<Turn> turns) {
        HealthArmorInfo info = new HealthArmorInfo();

        GenericRow header = new GenericRow();
        info.setHeader(header);
        header.addColumn(new GenericColumn("Player"));
        for (int i=1; i <= turns.size(); i++) {
            header.addColumn(new GenericColumn(""+turns.get(i-1).getTurnNumber()));
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
