package ca.utoronto.fitbook.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// The error thrown given a username not in the database
@ResponseStatus(value= HttpStatus.UNPROCESSABLE_ENTITY, reason="User Not Found")
public  class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User Not Found.");
    }
}