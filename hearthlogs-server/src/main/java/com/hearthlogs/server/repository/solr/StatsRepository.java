package com.hearthlogs.server.repository.solr;

import com.hearthlogs.server.match.Stats;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatsRepository extends SolrCrudRepository<Stats, String> {
}