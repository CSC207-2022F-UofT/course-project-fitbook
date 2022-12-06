package ca.utoronto.fitbook.application.exceptions;

import ca.utoronto.fitbook.application.port.in.command.UserRegisterCommand;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// The error thrown given a longer username than 40 chars
@ResponseStatus(value= HttpStatus.UNPROCESSABLE_ENTITY, reason="Name is too long")
public class NameTooLongException extends RuntimeException {
    public NameTooLongException(UserRegisterCommand command) {
        super("Name is too long by " + ((command.getName().length()) - 40) + " characters");
    }
}
