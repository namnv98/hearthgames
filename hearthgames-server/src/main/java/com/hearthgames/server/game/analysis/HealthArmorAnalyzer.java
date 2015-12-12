package com.hearthgames.server.game.analysis;

import com.hearthgames.server.game.analysis.domain.generic.GenericTable;
import com.hearthgames.server.game.play.domain.Turn;
import com.hearthgames.server.game.analysis.domain.generic.GenericColumn;
import com.hearthgames.server.game.analysis.domain.generic.GenericRow;
import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.play.GameResult;
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

        Integer friendlyHealth = getCurrentHealth(context.getFriendlyPlayer(), context);
        Integer opposingHealth = getCurrentHealth(context.getOpposingPlayer(), context);
        Integer friendlyArmor = getCurrentArmor(context.getFriendlyPlayer(), context);
        Integer opposingArmor = getCurrentArmor(context.getOpposingPlayer(), context);

        for (Turn turn: turns) {
            if (hasHealthChanged(context.getFriendlyPlayer(), turn.getActions())) {
                friendlyHealth = getHealth(context.getFriendlyPlayer(), turn.getActions());
            }
            if (hasArmorChanged(context.getFriendlyPlayer(), turn.getActions())) {
                friendlyArmor = getArmor(context.getFriendlyPlayer(), turn.getActions());
            }
            if (hasHealthChanged(context.getOpposingPlayer(), turn.getActions())) {
                opposingHealth = getHealth(context.getOpposingPlayer(), turn.getActions());
            }
            if (hasArmorChanged(context.getOpposingPlayer(), turn.getActions())) {
                opposingArmor = getArmor(context.getOpposingPlayer(), turn.getActions());
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
