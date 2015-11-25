package com.hearthlogs.server.database.repository;

import com.hearthlogs.server.database.domain.RawMatchError;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawMatchErrorRepository extends PagingAndSortingRepository<RawMatchError, Long> {

}
