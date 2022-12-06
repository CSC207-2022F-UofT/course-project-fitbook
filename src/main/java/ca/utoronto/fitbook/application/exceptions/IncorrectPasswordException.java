package ca.utoronto.fitbook.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// The error thrown given an incorrect password
@ResponseStatus(value= HttpStatus.UNAUTHORIZED, reason="Incorrect Password")
public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException() {
        super("Incorrect Password.");
    }
}
