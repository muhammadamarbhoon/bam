package com.account.bam.model.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByAccountId(String accountId);
    Account findByAccountNumber(String accountNumber);

}
