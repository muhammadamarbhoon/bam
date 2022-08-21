package com.account.bam.account;

import com.account.bam.data.dto.AccountResponse;
import com.account.bam.model.account.Account;
import com.account.bam.model.account.balance.Balance;
import com.account.bam.util.DateUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountMapper {
    private AccountMapper() {
    }

    public static AccountResponse toAccountResponse(Account account) {

        AccountResponse accountResponse = new AccountResponse();

        accountResponse.setAccountId(UUID.fromString(account.getAccountId()));
        accountResponse.setAccountNumber(account.getAccountNumber());
        accountResponse.setAccountTitle(account.getAccountTitle());
        accountResponse.setCreationTime(DateUtils.toOffsetDateTime(account.getCreationTime()));
        accountResponse.setUpdateTime(DateUtils.toOffsetDateTime(account.getUpdateTime()));
        accountResponse.setBalances(toBalanceResponse(account.getBalances()));
        return accountResponse;

    }

    private static List<com.account.bam.data.dto.Balance> toBalanceResponse(List<Balance> balanceList) {

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