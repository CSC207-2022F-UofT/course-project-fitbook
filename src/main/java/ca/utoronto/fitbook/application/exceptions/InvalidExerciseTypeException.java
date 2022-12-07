package ca.utoronto.fitbook.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Invalid exercise type")
public class InvalidExerciseTypeException extends RuntimeException {
    public InvalidExerciseTypeException(String type) {
        super(String.format("Exercise type (%s) is invalid.", type));
    }
}
