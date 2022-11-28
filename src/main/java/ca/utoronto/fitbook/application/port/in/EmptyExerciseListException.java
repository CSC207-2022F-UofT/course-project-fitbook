package ca.utoronto.fitbook.application.port.in;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNPROCESSABLE_ENTITY, reason="Exercise list empty")
public class EmptyExerciseListException extends RuntimeException{
    public EmptyExerciseListException() {
        super("Exercise list cannot be empty");
    }
}
