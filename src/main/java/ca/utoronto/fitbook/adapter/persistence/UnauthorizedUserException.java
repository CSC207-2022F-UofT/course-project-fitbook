package ca.utoronto.fitbook.adapter.persistence;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Unauthorized user.")
public class UnauthorizedUserException extends RuntimeException
{
    public UnauthorizedUserException() {
        super("Unauthorized user.");
    }
}
