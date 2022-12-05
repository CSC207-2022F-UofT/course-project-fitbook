package ca.utoronto.fitbook.application.port.out.response;

import ca.utoronto.fitbook.entity.Exercise;
import lombok.NonNull;
import lombok.Value;

import java.util.ArrayList;

@Value
public class ViewExerciseInfoResponse {

    @NonNull
    ArrayList<Exercise> exercises;
}
