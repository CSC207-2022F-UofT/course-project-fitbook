package ca.utoronto.fitbook.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@NoArgsConstructor
public class TemporalExercise extends Exercise {

    @Builder.Default
    private ExerciseType type = ExerciseType.TEMPORAL;
    private int time;

}
