package com.hearthlogs.server.repository.jpa;

import com.hearthlogs.server.match.Match;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends CrudRepository<Match, Long> {
}