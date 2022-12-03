package ca.utoronto.fitbook.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNPROCESSABLE_ENTITY, reason="Entity not found")
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String id) {
        super(String.format("Entity with id %s not found.", (id)));
    }
}