package ca.utoronto.fitbook.application.port.in;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Entity not found")
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String id) {
        super(String.format("Entity with id %s not found.", (id)));
    }
}