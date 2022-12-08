package ca.utoronto.fitbook.application.port.out.response;

import ca.utoronto.fitbook.entity.Post;
import ca.utoronto.fitbook.entity.RepetitiveExercise;
import ca.utoronto.fitbook.entity.TemporalExercise;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
public class SearchPostResponse {

    @NonNull
    String authorName;

    @NonNull
    Post post;

    @NonNull
    List<RepetitiveExercise> repetitiveExerciseList;

    @NonNull
    List<TemporalExercise> temporalExerciseList;

}
