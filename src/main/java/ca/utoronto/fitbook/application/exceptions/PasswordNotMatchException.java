package ca.utoronto.fitbook.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// The error thrown given two mismatch passwords
@ResponseStatus(value= HttpStatus.UNAUTHORIZED, reason="Password don't match")
public class PasswordNotMatchException extends RuntimeException {
    public PasswordNotMatchException() {
        super("Password don't match.");
    }
}
