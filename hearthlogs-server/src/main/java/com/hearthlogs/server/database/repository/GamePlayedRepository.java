package com.hearthlogs.server.database.repository;

import com.hearthlogs.server.database.domain.GamePlayed;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GamePlayedRepository extends PagingAndSortingRepository<GamePlayed, Long> {

    List<GamePlayed> findByFriendlyGameAccountIdAndOpposingGameAccountId(String friendlyGameAccountId, String opposingGameAccountId);

    List<GamePlayed> findByFriendlyGameAccountId(String friendlyGameAccountId);

    GamePlayed findFirstByBattletag(String battletag);

}
