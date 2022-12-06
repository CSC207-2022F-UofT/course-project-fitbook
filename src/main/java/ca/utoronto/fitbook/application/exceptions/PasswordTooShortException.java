package ca.utoronto.fitbook.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// The error thrown given a password smaller than 8 chars
@ResponseStatus(value= HttpStatus.UNPROCESSABLE_ENTITY, reason="Password is too short")
public class PasswordTooShortException extends RuntimeException {
    public PasswordTooShortException() {
        super("Password is too short");
    }
}
