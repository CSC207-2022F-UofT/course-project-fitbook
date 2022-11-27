package ca.utoronto.fitbook.application.port.in;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNPROCESSABLE_ENTITY, reason="Entity not found")
public class ExerciseInListNotFoundException extends RuntimeException {
    public ExerciseInListNotFoundException() {
        super("Some exercise in list not found.");
    }
}
