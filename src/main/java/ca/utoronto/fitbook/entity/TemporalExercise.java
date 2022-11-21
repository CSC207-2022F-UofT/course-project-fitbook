package ca.utoronto.fitbook.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@NoArgsConstructor
public class TemporalExercise extends Exercise {

    @Builder.Default
    private ExerciseType type = ExerciseType.TEMPORAL;
    @NonNull
    private Date time;

}
