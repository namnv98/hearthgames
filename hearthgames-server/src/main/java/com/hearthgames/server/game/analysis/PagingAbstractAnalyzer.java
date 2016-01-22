package com.hearthgames.server.game.analysis;

import com.hearthgames.server.game.log.domain.RawGameData;
import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.play.GameResult;
import com.hearthgames.server.game.play.domain.Turn;

import java.util.ArrayList;
import java.util.List;

public abstract class PagingAbstractAnalyzer<T> implements Analyzer<List<T>> {

    public List<T> analyze(GameResult result, GameContext context, RawGameData rawGameData) {

        List<T> infos = new ArrayList<>();

        List<Turn> subSetOfTurns = new ArrayList<>();
        for (Turn turn: result.getTurns()) {
            if (turn.getTurnNumber() != 0) {
                if (turn.getTurnNumber() % 24 == 0) {
                    subSetOfTurns.add(turn);
                    T info = getInfo(result, context, rawGameData, subSetOfTurns);
                    infos.add(info);
                    subSetOfTurns = new ArrayList<>();
                } else {
                    subSetOfTurns.add(turn);
                }
            }
        }
        if (subSetOfTurns.size() > 0) {
            T info = getInfo(result, context, rawGameData, subSetOfTurns);
            infos.add(info);
        }
        return infos;
    }

    protected abstract T getInfo(GameResult result, GameContext context, RawGameData rawGameData, List<Turn> turns);
}
