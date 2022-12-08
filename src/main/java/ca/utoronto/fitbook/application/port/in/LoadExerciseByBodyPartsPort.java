package ca.utoronto.fitbook.application.port.in;

import ca.utoronto.fitbook.entity.Exercise;

import java.util.List;

public interface LoadExerciseByBodyPartsPort
{
    List<Exercise> loadExerciseByBodyParts(List<String> bodyParts);
}
