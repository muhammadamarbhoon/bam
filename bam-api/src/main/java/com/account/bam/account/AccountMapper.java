package com.account.bam.account;

import com.account.bam.data.dto.AccountResponse;
import com.account.bam.model.account.Account;
import com.account.bam.model.account.balance.Balance;
import com.account.bam.util.DateUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AccountMapper {

    private final CurrencyConversionService currencyConversionService;

    private AccountMapper(CurrencyConversionService currencyConversionService) {
        this.currencyConversionService = currencyConversionService;
    }

    public AccountResponse toAccountResponse(Account account, String baseCurrency) {

        AccountResponse accountResponse = new AccountResponse();

        accountResponse.setAccountId(UUID.fromString(account.getAccountId()));
        accountResponse.setAccountNumber(account.getAccountNumber());
        accountResponse.setAccountTitle(account.getAccountTitle());
        accountResponse.setCreationTime(DateUtils.toOffsetDateTime(account.getCreationTime()));
        accountResponse.setUpdateTime(DateUtils.toOffsetDateTime(account.getUpdateTime()));

        accountResponse.setCumulativeBalance(currencyConversionService
                .calculateCumulativeBalance(account.getBalances(), baseCurrency));

        accountResponse.setBalances(toBalanceResponse(account.getBalances()));
        return accountResponse;

    }

    private List<com.account.bam.data.dto.Balance> toBalanceResponse(List<Balance> balanceList) {

        List<com.account.bam.data.dto.Balance> balances = new ArrayList<>();
        for (Balance balance : balanceList) {
            com.account.bam.data.dto.Balance balanceResponse = new com.account.bam.data.dto.Balance();
            balanceResponse.setAmount(balance.getAmount());
            balanceResponse.setCurrency(balance.getCurrency());
            balances.add(balanceResponse);
        }
        return balances;
    }
}