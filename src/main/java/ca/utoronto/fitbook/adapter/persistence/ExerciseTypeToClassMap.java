package ca.utoronto.fitbook.adapter.persistence;

import ca.utoronto.fitbook.entity.Exercise;
import ca.utoronto.fitbook.entity.RepetitiveExercise;
import ca.utoronto.fitbook.entity.TemporalExercise;

import java.util.HashMap;
import java.util.Map;

public class ExerciseTypeToClassMap
{
    private static final Map<Exercise.ExerciseType, String> map = new HashMap<>() {{
            put(Exercise.ExerciseType.TEMPORAL, TemporalExercise.class.getName());
            put(Exercise.ExerciseType.REPETITIVE, RepetitiveExercise.class.getName());
    }};

    public static String get(Exercise.ExerciseType type) {
        return map.get(type);
    }
}
