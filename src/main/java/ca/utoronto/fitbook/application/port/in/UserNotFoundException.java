package ca.utoronto.fitbook.application.port.in;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNPROCESSABLE_ENTITY, reason="User not found")
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String id) {
        super(String.format("User with id %s not found.", (id)));
    }
}
