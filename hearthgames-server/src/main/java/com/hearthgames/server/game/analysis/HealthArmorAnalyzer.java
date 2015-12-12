package com.hearthgames.server.game.analysis;

import com.hearthgames.server.game.analysis.domain.generic.GenericTable;
import com.hearthgames.server.game.play.domain.Turn;
import com.hearthgames.server.game.analysis.domain.generic.GenericColumn;
import com.hearthgames.server.game.analysis.domain.generic.GenericRow;
import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.play.GameResult;
import com.hearthgames.server.util.HeroStatsUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HealthArmorAnalyzer extends PagingAbstractAnalyzer<GenericTable> {

    protected GenericTable getInfo(GameResult result, GameContext context, List<Turn> turns) {
        GenericTable table = new GenericTable();

        GenericRow header = new GenericRow();
        table.setHeader(header);
        header.addColumn(new GenericColumn(""));
        for (Turn turn: turns) {
            header.addColumn(new GenericColumn(""+turn.getTurnNumber()));
        }

        GenericRow friendly = new GenericRow();
        table.setFriendly(friendly);
        friendly.addColumn(new GenericColumn(context.getFriendlyPlayer().getName()));

        GenericRow opposing = new GenericRow();
        table.setOpposing(opposing);
        opposing.addColumn(new GenericColumn(context.getOpposingPlayer().getName()));

        Integer friendlyHealth = HeroStatsUtil.getCurrentHealth(context.getFriendlyPlayer(), context);
        Integer opposingHealth = HeroStatsUtil.getCurrentHealth(context.getOpposingPlayer(), context);
        Integer friendlyArmor = HeroStatsUtil.getCurrentArmor(context.getFriendlyPlayer(), context);
        Integer opposingArmor = HeroStatsUtil.getCurrentArmor(context.getOpposingPlayer(), context);

        for (Turn turn: turns) {
            if (HeroStatsUtil.hasHealthChanged(context.getFriendlyPlayer(), turn.getActions())) {
                friendlyHealth = HeroStatsUtil.getHealth(context.getFriendlyPlayer(), turn.getActions());
            }
            if (HeroStatsUtil.hasArmorChanged(context.getFriendlyPlayer(), turn.getActions())) {
                friendlyArmor = HeroStatsUtil.getArmor(context.getFriendlyPlayer(), turn.getActions());
            }
            if (HeroStatsUtil.hasHealthChanged(context.getOpposingPlayer(), turn.getActions())) {
                opposingHealth = HeroStatsUtil.getHealth(context.getOpposingPlayer(), turn.getActions());
            }
            if (HeroStatsUtil.hasArmorChanged(context.getOpposingPlayer(), turn.getActions())) {
                opposingArmor = HeroStatsUtil.getArmor(context.getOpposingPlayer(), turn.getActions());
            }

            GenericColumn col = new GenericColumn(""+friendlyHealth);
            if (friendlyArmor != null && friendlyArmor != 0) {
                col.setExtraData(""+friendlyArmor);
            }
            friendly.addColumn(col);

            col = new GenericColumn(""+opposingHealth);
            if (opposingArmor != null && opposingArmor != 0) {
                col.setExtraData(""+opposingArmor);
            }
            opposing.addColumn(col);
        }

        return table;
    }
}
