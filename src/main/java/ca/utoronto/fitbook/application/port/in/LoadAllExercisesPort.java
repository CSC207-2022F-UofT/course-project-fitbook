package ca.utoronto.fitbook.application.port.in;

import ca.utoronto.fitbook.entity.Exercise;

import java.util.ArrayList;

public interface LoadAllExercisesPort {
    ArrayList<Exercise> loadAllExercises();
}
