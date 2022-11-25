package ca.utoronto.fitbook.application.port.in;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Username collision.")
public class UsernameNotFoundException extends RuntimeException
{
    public UsernameNotFoundException(String name) {
        super(String.format("User with name %s not found.", name));
    }
}
