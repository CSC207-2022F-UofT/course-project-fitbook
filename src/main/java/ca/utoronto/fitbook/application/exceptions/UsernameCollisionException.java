package ca.utoronto.fitbook.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Username collision.")
public class UsernameCollisionException extends RuntimeException
{
    public UsernameCollisionException(String name) {
        super(String.format("More than one user with name %s found.", name));
    }
}
