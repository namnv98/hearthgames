package com.hearthgames.server.database.repository;

import com.hearthgames.server.database.domain.ArenaRun;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArenaRunRepository extends PagingAndSortingRepository<ArenaRun, Long> {

    List<ArenaRun> findByGameAccountId(String gameAccountId);

}
