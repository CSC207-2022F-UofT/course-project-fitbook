package ca.utoronto.fitbook.application.port.in;

import ca.utoronto.fitbook.entity.Exercise;

import java.util.List;

public interface LoadExerciseListPort
{
    List<Exercise> loadExerciseList(List<String> exerciseIds) throws EntityNotFoundException;
}
