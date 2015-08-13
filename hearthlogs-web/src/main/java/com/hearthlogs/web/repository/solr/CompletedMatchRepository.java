package com.hearthlogs.web.repository.solr;

import com.hearthlogs.web.match.CompletedMatch;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletedMatchRepository extends SolrCrudRepository<CompletedMatch, String> {
}