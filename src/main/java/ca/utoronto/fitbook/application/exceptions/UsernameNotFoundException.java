package ca.utoronto.fitbook.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Username not found.")
public class UsernameNotFoundException extends RuntimeException
{
    public UsernameNotFoundException(String name) {
        super(String.format("User with name %s not found.", name));
    }
}
