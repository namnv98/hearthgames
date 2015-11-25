package com.hearthlogs.server.database.repository;

import com.hearthlogs.server.database.domain.BetaSignUp;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BetaSignUpRepository extends PagingAndSortingRepository<BetaSignUp, Long> {

    List<BetaSignUp> findByApproved(boolean approved);

    BetaSignUp findByBattletag(String battletag);

}
