package com.hearthgames.server.database.repository;

import com.hearthgames.server.database.domain.PlayerWinLossSummary;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerWinLossSummaryRepository extends PagingAndSortingRepository<PlayerWinLossSummary, Long> {

}
