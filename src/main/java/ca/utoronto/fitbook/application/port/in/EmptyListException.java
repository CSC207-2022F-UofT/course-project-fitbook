package ca.utoronto.fitbook.application.port.in;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Exercise list empty")
public class EmptyListException extends RuntimeException{
    public EmptyListException() {
        super("Exercise list cannot be empty");
    }
}
