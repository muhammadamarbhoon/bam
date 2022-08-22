package com.account.bam.account;

import static java.util.Objects.nonNull;
import com.account.bam.core.ApplicationError;
import com.account.bam.core.ApplicationException;
import com.account.bam.model.account.balance.Balance;
import com.account.bam.model.account.currencyrate.CurrencyRate;
import com.account.bam.model.account.currencyrate.CurrencyRateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class CurrencyConversionService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final CurrencyRateRepository currencyRateRepository;

    private CurrencyConversionService(CurrencyRateRepository currencyRateRepository) {
        this.currencyRateRepository = currencyRateRepository;
    }

    public BigDecimal calculateCumulativeBalance(List<Balance> balanceList, String currencyFrom) {
        BigDecimal cumulativeBalance = BigDecimal.ZERO;
        for (Balance balance : balanceList) {
            String currencyTo = balance.getCurrency();
            CurrencyRate currencyRate = currencyRateRepository
                    .findByBaseCcyAndTargetCcy(currencyFrom, currencyTo);

            if (nonNull(currencyRate) && currencyRate.getRate().compareTo(BigDecimal.ZERO) > 0) {
                cumulativeBalance = cumulativeBalance
                        .add(currencyRate.getRate().multiply(balance.getAmount()));
            } else {
                log.warn("Currency conversion rate from {} to {} does not exist", currencyFrom, currencyTo);
                throw new ApplicationException(ApplicationError.CURRENCY_CONVERSION_RATE_MISSING);
            }
        }
        return cumulativeBalance;
    }
}
