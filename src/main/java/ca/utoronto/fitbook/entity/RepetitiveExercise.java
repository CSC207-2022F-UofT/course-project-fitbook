package ca.utoronto.fitbook.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@NoArgsConstructor
public class RepetitiveExercise extends Exercise {

    @Builder.Default
    private ExerciseType type = ExerciseType.REPETITIVE;
    private int reps;
    private int sets;

}
