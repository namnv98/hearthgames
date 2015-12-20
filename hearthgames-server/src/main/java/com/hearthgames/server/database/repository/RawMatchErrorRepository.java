package com.hearthgames.server.database.repository;

import com.hearthgames.server.database.domain.RawGameError;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawMatchErrorRepository extends PagingAndSortingRepository<RawGameError, Long> {

}
