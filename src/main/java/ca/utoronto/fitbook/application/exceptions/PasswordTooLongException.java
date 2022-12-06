package ca.utoronto.fitbook.application.exceptions;

import ca.utoronto.fitbook.application.port.in.command.UserRegisterCommand;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// The error thrown given a password longer than 40 chars
@ResponseStatus(value= HttpStatus.UNPROCESSABLE_ENTITY, reason="Password is too long")
public class PasswordTooLongException extends RuntimeException {
    public PasswordTooLongException(UserRegisterCommand command) {
        super("Password is too long by " + ((command.getName().length()) - 40) + " characters");
    }
}
