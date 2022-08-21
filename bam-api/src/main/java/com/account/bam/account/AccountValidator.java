package com.account.bam.account;

import com.account.bam.core.ApplicationError;
import com.account.bam.core.ApplicationException;
import com.account.bam.data.dto.UpdateAccBalanceRequest;

public class AccountValidator {

    public static void validateAccountUpdateRequest(
            UpdateAccBalanceRequest updateAccBalanceRequest) {

        if (updateAccBalanceRequest.getAmount() == null
                || updateAccBalanceRequest.getCurrency() == null
                || updateAccBalanceRequest.getCrdDbtInd() == null) {

            throw new ApplicationException(ApplicationError.BAD_REQUEST);
        }
    }
}
