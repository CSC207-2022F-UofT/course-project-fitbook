package ca.utoronto.fitbook.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNPROCESSABLE_ENTITY, reason="Description max length reached")
public class DescriptionTooLongException extends RuntimeException{
    public DescriptionTooLongException() {
        super("The description cannot be longer than 100 characters");
    }
}
