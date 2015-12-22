package com.hearthgames.server.database.repository;

import com.hearthgames.server.database.domain.Account;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {

    Account findFirstByBattletag(String battletag);

}
