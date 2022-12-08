package ca.utoronto.fitbook.application.port.out.response;

import ca.utoronto.fitbook.entity.Post;
import ca.utoronto.fitbook.entity.RepetitiveExercise;
import ca.utoronto.fitbook.entity.TemporalExercise;
import lombok.*;

import java.util.List;

@Value
public class PostResponse {
    @NonNull
    Post post;
    @NonNull
    String authorName;
    @NonNull
    List<RepetitiveExercise> repetitiveExerciseList;
    @NonNull
    List<TemporalExercise> temporalExerciseList;
    boolean userLiked;
}
