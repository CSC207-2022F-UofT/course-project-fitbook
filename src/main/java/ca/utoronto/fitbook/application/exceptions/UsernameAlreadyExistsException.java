package ca.utoronto.fitbook.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// The error thrown given a username already in the database
@ResponseStatus(value= HttpStatus.CONFLICT, reason="Username already exists")
public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException() {
        super("Username already exists.");
    }
}
