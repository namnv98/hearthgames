package com.hearthgames.server.game.analysis;

import com.hearthgames.server.game.analysis.domain.generic.GenericColumn;
import com.hearthgames.server.game.analysis.domain.generic.GenericRow;
import com.hearthgames.server.game.analysis.domain.generic.GenericTable;
import com.hearthgames.server.game.log.domain.RawGameData;
import com.hearthgames.server.game.parse.GameState;
import com.hearthgames.server.game.play.GameResult;
import com.hearthgames.server.game.play.domain.Turn;
import com.hearthgames.server.util.HeroStatsUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HealthArmorAnalyzer extends PagingAbstractAnalyzer<GenericTable> {

    protected GenericTable getInfo(GameResult result, GameState gameState, RawGameData rawGameData, List<Turn> turns) {
        GenericTable table = new GenericTable();

        GenericRow header = new GenericRow();
        table.setHeader(header);
        header.addColumn(new GenericColumn(""));
        for (Turn turn: turns) {
            header.addColumn(new GenericColumn(""+turn.getTurnNumber()));
        }

        GenericRow friendly = new GenericRow();
        table.setFriendly(friendly);
        friendly.addColumn(new GenericColumn(gameState.getFriendlyPlayer().getName()));

        GenericRow opposing = new GenericRow();
        table.setOpposing(opposing);
        opposing.addColumn(new GenericColumn(gameState.getOpposingPlayer().getName()));

        Integer friendlyHealth = HeroStatsUtil.getCurrentHealth(gameState.getFriendlyPlayer(), gameState);
        Integer opposingHealth = HeroStatsUtil.getCurrentHealth(gameState.getOpposingPlayer(), gameState);
        Integer friendlyArmor = HeroStatsUtil.getCurrentArmor(gameState.getFriendlyPlayer(), gameState);
        Integer opposingArmor = HeroStatsUtil.getCurrentArmor(gameState.getOpposingPlayer(), gameState);

        for (Turn turn: turns) {
            if (HeroStatsUtil.hasHealthChanged(gameState.getFriendlyPlayer(), turn.getActions())) {
                friendlyHealth = HeroStatsUtil.getHealth(gameState.getFriendlyPlayer(), turn.getActions());
            }
            if (HeroStatsUtil.hasArmorChanged(gameState.getFriendlyPlayer(), turn.getActions())) {
                friendlyArmor = HeroStatsUtil.getArmor(gameState.getFriendlyPlayer(), turn.getActions());
            }
            if (HeroStatsUtil.hasHealthChanged(gameState.getOpposingPlayer(), turn.getActions())) {
                opposingHealth = HeroStatsUtil.getHealth(gameState.getOpposingPlayer(), turn.getActions());
            }
            if (HeroStatsUtil.hasArmorChanged(gameState.getOpposingPlayer(), turn.getActions())) {
                opposingArmor = HeroStatsUtil.getArmor(gameState.getOpposingPlayer(), turn.getActions());
            }

            GenericColumn col = new GenericColumn(""+friendlyHealth);
            if (friendlyArmor != null && friendlyArmor != 0) {
                col.setExtraData(""+friendlyArmor);
            }
            if (friendlyHealth != null && friendlyHealth < 30) {
                col.setExtraData2("damaged");
            }
            friendly.addColumn(col);

            col = new GenericColumn(""+opposingHealth);
            if (opposingArmor != null && opposingArmor != 0) {
                col.setExtraData(""+opposingArmor);
            }
            if (opposingHealth != null && opposingHealth < 30) {
                col.setExtraData2("damaged");
            }
            opposing.addColumn(col);
        }

        return table;
    }
}
