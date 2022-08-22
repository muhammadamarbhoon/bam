package com.account.bam.model.account.currencyrate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {

    CurrencyRate findByBaseCcyAndTargetCcy(String baseCcy, String targetCcy);
}
