package com.hearthlogs.web.repository.jpa;

import com.hearthlogs.web.match.RawMatch;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawMatchRepository extends CrudRepository<RawMatch, Long> {
}