package com.account.bam.core;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

public enum ApplicationError {

    FORBIDDEN(HttpStatus.FORBIDDEN, "BAM-00", "Action Foridden", Level.ERROR),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAM-01", "Bad Request", Level.ERROR),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "BAM-02", "Server Error", Level.ERROR),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "BAM-03", "Missing request parameter", Level.ERROR),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "BAM-04", "Invalid value for one or more parameters in request",
            Level.WARN),
    HTTP_METHOD_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "BAM-05", "HTTP Method not supported", Level.ERROR),
    MISSING_REQUEST_BODY(HttpStatus.BAD_REQUEST, "BAM-06", "Required request body is missing", Level.ERROR),
    INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST, "BAM-07", "Insufficient balance", Level.INFO),
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "BAM-08", "Account not found", Level.ERROR),
    NO_FUNDS_AGAINST_GIVEN_CURRENCY(HttpStatus.BAD_REQUEST, "BAM-09", "No funds against given currency", Level.INFO),
    DEBITING_SIMULATION_CALL_FAILED(HttpStatus.FAILED_DEPENDENCY, "BAM-10", "Debiting simulation call failed", Level.WARN);


    private HttpStatus httpStatus;
    private String code;
    private String message;
    private Level logLevel;

    ApplicationError(HttpStatus httpStatus, String code, String message, Level logLevel) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
        this.logLevel = logLevel;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Level getLogLevel() {
        return logLevel;
    }
}
