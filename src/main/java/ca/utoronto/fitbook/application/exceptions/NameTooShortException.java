package ca.utoronto.fitbook.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// The error thrown given a username shorter than 3 chars
@ResponseStatus(value= HttpStatus.UNPROCESSABLE_ENTITY, reason="Name is too short")
public class NameTooShortException extends RuntimeException {
    public NameTooShortException() {
        super("Name is too short");
    }
}
