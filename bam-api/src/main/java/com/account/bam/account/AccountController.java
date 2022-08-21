package com.account.bam.account;

import com.account.bam.data.dto.AccountRequest;
import com.account.bam.data.dto.AccountResponse;
import com.account.bam.data.dto.UpdateAccBalanceRequest;
import com.account.bam.util.JsonUtils;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class AccountController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/v1/accounts")
    public ResponseEntity<Void> createAccount(@RequestBody AccountRequest accountRequest) {

        log.debug("Request: POST /api/v1/accounts");
        log.debug("Request Body: {}", JsonUtils.formatJson(accountRequest));

        String accountId = accountService.createAccount(accountRequest);

        String accountLocation = "/api/v1/accounts/" + accountId;

        log.debug("Location: {}", accountLocation);

        return ResponseEntity.status(HttpStatus.CREATED).header("Location", accountLocation).body(null);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/v1/accounts/{account-id}/balance")
    public ResponseEntity<Void> updateAccountBalance(
            @PathVariable(value = "account-id") UUID accountId,
            @RequestBody UpdateAccBalanceRequest updateAccBalanceRequest) {

        log.debug("Request: PUT /api/v1/accounts/{}/balance", accountId);
        log.debug("Request Body: {}", JsonUtils.formatJson(updateAccBalanceRequest));

        accountService.updateAccountBalance(accountId.toString(), updateAccBalanceRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/api/v1/accounts/{account-id}")
    public ResponseEntity<AccountResponse> getAccountDetails(
            @PathVariable(value = "account-id") UUID accountId) {

        log.debug("Request: GET /api/v1/accounts/{}", accountId);

        AccountResponse response = accountService.getAccountDetails(accountId.toString());

        log.debug("Response Body: {}", JsonUtils.formatJson(response));

        return ResponseEntity.ok(response);

    }

}
