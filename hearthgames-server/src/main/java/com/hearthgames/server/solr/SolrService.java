package com.hearthgames.server.solr;

import com.hearthgames.server.database.domain.GamePlayed;
import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.play.GameResult;
import com.hearthgames.server.solr.domain.IndexedGamePlayed;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SolrService {

    private SolrClient solr = new HttpSolrClient("http://localhost:8983/solr/hearthgames");

    public void indexGame(GamePlayed gamePlayed, GameContext context, GameResult result) throws IOException, SolrServerException {

        IndexedGamePlayed game = new IndexedGamePlayed();
        game.setId(gamePlayed.getId()+"");
        game.setFriendlyGameAccountId(gamePlayed.getFriendlyGameAccountId());
        game.setFriendlyName(gamePlayed.getFriendlyName());
        game.setFriendlyClass(gamePlayed.getFriendlyClass());
        game.setFriendlyClassWon(context.getFriendlyPlayer() == result.getWinner());
        game.setFriendlyClassQuit(context.getFriendlyPlayer() == result.getQuitter());
        game.setOpposingGameAccountId(gamePlayed.getOpposingGameAccountId());
        game.setOpposingName(gamePlayed.getOpposingName());
        game.setOpposingClass(gamePlayed.getOpposingClass());
        game.setOpposingClassWon(context.getOpposingPlayer() == result.getWinner());
        game.setOpposingClassQuit(context.getOpposingPlayer() == result.getQuitter());
        game.setRank(gamePlayed.getRank());
        game.setTurns(gamePlayed.getTurns());
        game.setStartTime(Timestamp.valueOf(gamePlayed.getStartTime()));
        game.setEndTime(Timestamp.valueOf(gamePlayed.getEndTime()));
        List<String> friendlyStartingCards = result.getFriendlyStartingCards().stream().filter(card -> !StringUtils.isEmpty(card.getCardid())).map(Card::getCardid).collect(Collectors.toList());
        List<String> friendlyMulliganCards = result.getFriendlyMulliganedCards().stream().filter(card -> !StringUtils.isEmpty(card.getCardid())).map(Card::getCardid).collect(Collectors.toList());
        List<String> friendlyDeckCards = result.getFriendlyDeckCards().stream().filter(card -> !StringUtils.isEmpty(card.getCardid())).map(Card::getCardid).collect(Collectors.toList());
        game.setFriendlyStartingCards(friendlyStartingCards);
        game.setFriendlyMulliganCards(friendlyMulliganCards);
        game.setFriendlyDeckCards(friendlyDeckCards);
        List<String> opposingStartingCards = result.getOpposingStartingCards().stream().filter(card -> !StringUtils.isEmpty(card.getCardid())).map(Card::getCardid).collect(Collectors.toList());
        List<String> opposingMulliganCards = result.getOpposingMulliganedCards().stream().filter(card -> !StringUtils.isEmpty(card.getCardid())).map(Card::getCardid).collect(Collectors.toList());
        List<String> opposingDeckCards = result.getOpposingDeckCards().stream().filter(card -> !StringUtils.isEmpty(card.getCardid())).map(Card::getCardid).collect(Collectors.toList());
        game.setOpposingStartingCards(opposingStartingCards);
        game.setOpposingMulliganCards(opposingMulliganCards);
        game.setOpposingDeckCards(opposingDeckCards);

        game.setMatchup(gamePlayed.getFriendlyClass()+"_"+gamePlayed.getOpposingClass());

        DocumentObjectBinder dob = new DocumentObjectBinder();
        SolrInputDocument doc = dob.toSolrInputDocument(game);

        UpdateResponse response = solr.add(doc);

        solr.commit();
    }


//    public void search() throws IOException, SolrServerException {
//
////        SolrQuery query = new SolrQuery();
////        query.setQuery("foo");
////
////        query.setHighlight(true).setHighlightSnippets(1); //set other params as needed
////        query.setParam("hl.fl", "content");
////
//        SolrQuery query = new SolrQuery();
//        query.setQuery("*:*");
//
//        QueryResponse response = solr.query(query);
//
//
//        SolrDocumentList list = response.getResults();
//
//        query = new  SolrQuery().
//                setQuery("+matchup_s:Hunter_Paladin").
//                setFacet(true).
//                setFacetMinCount(1).
//                setFacetLimit(8).
//                addFacetField("matchup_s");
//
//        response = solr.query(query);
//
//
//        list = response.getResults();
//
//        DocumentObjectBinder dob = new DocumentObjectBinder();
//
//
//        List<IndexedGamePlayed> gamesPlayed = new ArrayList<>();
//        for (SolrDocument doc: list) {
//            gamesPlayed.add(dob.getBean(IndexedGamePlayed.class, doc));
//        }
//
//        System.out.println();
//
//
//    }


}
