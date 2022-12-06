package ca.utoronto.fitbook.application.port.out.response;

import ca.utoronto.fitbook.entity.RepetitiveExercise;
import ca.utoronto.fitbook.entity.TemporalExercise;
import ca.utoronto.fitbook.entity.User;
import lombok.*;

import java.util.List;

@Value
public class ProfilePostResponse {
    @NonNull
    String postId;
    @NonNull
    User author;
    int likes;
    @NonNull
    String postDate;
    @NonNull
    List<RepetitiveExercise> repetitiveExerciseList;
    @NonNull
    List<TemporalExercise> temporalExerciseList;
    @NonNull
    String description;
    boolean userLiked;
}
