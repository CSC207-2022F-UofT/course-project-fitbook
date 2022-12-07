package ca.utoronto.fitbook.application.port.out.response;

import ca.utoronto.fitbook.entity.RepetitiveExercise;
import ca.utoronto.fitbook.entity.TemporalExercise;
import lombok.NonNull;
import lombok.Value;

import java.util.ArrayList;

@Value
public class ViewExerciseInfoResponse {
    @NonNull
    ArrayList<TemporalExercise> temporalExercises;

    @NonNull
    ArrayList<RepetitiveExercise> repetitiveExercises;
}
