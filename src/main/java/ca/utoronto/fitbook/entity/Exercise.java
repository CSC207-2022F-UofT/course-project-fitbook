package ca.utoronto.fitbook.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@SuperBuilder
@Data
@NoArgsConstructor
public abstract class Exercise {

    public enum ExerciseType {
        TEMPORAL,
        REPETITIVE
    }

    @Builder.Default
    @NonNull
    protected String id = UUID.randomUUID().toString();
    protected ExerciseType type;
    @NonNull
    protected List<String> keywords;
    @NonNull
    protected List<String> bodyParts;

}
