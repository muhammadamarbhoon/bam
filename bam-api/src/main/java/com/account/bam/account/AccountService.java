package com.account.bam.account;

import static java.util.Objects.nonNull;

import com.account.bam.client.ClientService;
import com.account.bam.core.ApplicationError;
import com.account.bam.core.ApplicationException;
import com.account.bam.data.dto.AccountRequest;
import com.account.bam.data.dto.AccountResponse;
import com.account.bam.data.dto.UpdateAccBalanceRequest;
import com.account.bam.model.account.Account;
import com.account.bam.model.account.AccountRepository;
import com.account.bam.model.account.balance.Balance;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class AccountService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final ClientService clientService;

    public AccountService(AccountRepository accountRepository,
                          AccountMapper accountMapper, ClientService clientService) {

        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.clientService = clientService;
    }

    public String createAccount(AccountRequest accountRequest) {

        Account existingAccount = accountRepository
                .findByAccountNumber(accountRequest.getAccountNumber());

        if (nonNull(existingAccount)) {
            throw new ApplicationException(ApplicationError.ACCOUNT_ALREADY_EXIST);
        }
        
        Account account = new Account();
        account.setAccountId(UUID.randomUUID().toString());
        account.setAccountNumber(accountRequest.getAccountNumber());
        account.setAccountTitle(accountRequest.getAccountTitle());
        account.setCreationTime(LocalDateTime.now());
        account.setUpdateTime(LocalDateTime.now());

        account = accountRepository.save(account);

        log.debug("Account successfully created");

        return account.getAccountId();

    }

    public void updateAccountBalance(String accountId, UpdateAccBalanceRequest updateAccBalanceRequest) {

        AccountValidator.validateAccountUpdateRequest(updateAccBalanceRequest);

        Account account = accountRepository.findByAccountId(accountId);

        if (ObjectUtils.isEmpty(account)) {
            log.error("Account with ID {} does not exist", accountId);
            throw new ApplicationException(ApplicationError.ACCOUNT_NOT_FOUND);
        }

        if (UpdateAccBalanceRequest.CrdDbtIndEnum.CREDIT.getValue()
                .equals(updateAccBalanceRequest.getCrdDbtInd().getValue())) {

            creditBalanceInAccount(account, updateAccBalanceRequest);
        } else {
            debitAccountBalance(account, updateAccBalanceRequest);
        }

        account.setUpdateTime(LocalDateTime.now());

        accountRepository.save(account);
        log.debug("Account Balance successfully updated");

    }

    private void creditBalanceInAccount(Account account, UpdateAccBalanceRequest updateAccBalanceRequest) {
        Optional<Balance> balance = getBalanceByCcyFromAcc(account, updateAccBalanceRequest);

        if (balance.isPresent()) {
            Balance existingBalance = balance.get();
            BigDecimal currBalAmt = existingBalance.getAmount();
            BigDecimal updatedBalAmt = currBalAmt.add(updateAccBalanceRequest.getAmount().abs());
            existingBalance.setAmount(updatedBalAmt);
        } else {
            Balance newCurrencyBalance = buildNewBalance(account, updateAccBalanceRequest);
            account.getBalances().add(newCurrencyBalance);
        }
    }

    private Balance buildNewBalance(
            Account account, UpdateAccBalanceRequest updateAccBalanceRequest) {

        Balance newCurrencyBalance = new Balance();
        newCurrencyBalance.setCurrency(updateAccBalanceRequest.getCurrency());
        newCurrencyBalance.setAmount(updateAccBalanceRequest.getAmount());
        newCurrencyBalance.setCreationTime(LocalDateTime.now());
        newCurrencyBalance.setUpdateTime(LocalDateTime.now());
        newCurrencyBalance.setAccount(account);
        return newCurrencyBalance;
    }

    private void debitAccountBalance(Account account, UpdateAccBalanceRequest updateAccBalanceRequest) {

        Optional<Balance> balance = getBalanceByCcyFromAcc(account, updateAccBalanceRequest);

        if (balance.isPresent()) {
            Balance existingBalance = balance.get();
            BigDecimal currBalAmt = existingBalance.getAmount();
            String externalCallStatus = clientService.invokeExternalWebService();
            if (externalCallStatus.equals("success")) {
                BigDecimal updatedBalAmt = currBalAmt.subtract(updateAccBalanceRequest.getAmount().abs());
                if (updatedBalAmt.compareTo(BigDecimal.ZERO) < 0) {
                    log.info("Your account {} does not have sufficient balance against currency {} ",
                            account.getAccountNumber(), updateAccBalanceRequest.getCurrency());
                    throw new ApplicationException(ApplicationError.INSUFFICIENT_BALANCE);
                }
                existingBalance.setAmount(updatedBalAmt);
            } else {
                log.warn("Debiting simulation external call failed");
                throw new ApplicationException(ApplicationError.DEBITING_SIMULATION_CALL_FAILED);
            }
        } else {
            log.info("Your account {} does not have funds against given currency {} ",
                    account.getAccountNumber(), updateAccBalanceRequest.getCurrency());
            throw new ApplicationException(ApplicationError.NO_FUNDS_AGAINST_GIVEN_CURRENCY);
        }
    }

    private Optional<Balance> getBalanceByCcyFromAcc(
            Account account, UpdateAccBalanceRequest updateAccBalanceRequest) {
        return account.getBalances()
                .stream()
                .filter(bal-> updateAccBalanceRequest.getCurrency().equals(bal.getCurrency()))
                .findFirst();
    }

    public AccountResponse getAccountDetails(String accountId, String baseCurrency) {

        Account account = accountRepository.findByAccountId(accountId);

        if (ObjectUtils.isEmpty(account)) {
            log.error("Account with ID {} does not exist", accountId);
            throw new ApplicationException(ApplicationError.ACCOUNT_NOT_FOUND);
        }
        return accountMapper.toAccountResponse(account, baseCurrency);
    }
}
