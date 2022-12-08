package ca.utoronto.fitbook.application.service;


import ca.utoronto.fitbook.application.exceptions.InvalidExerciseTypeException;
import ca.utoronto.fitbook.application.port.in.LoadAllExercisesPort;
import ca.utoronto.fitbook.application.port.in.ViewExerciseInfoUseCase;
import ca.utoronto.fitbook.application.port.out.response.ViewExerciseInfoResponse;
import ca.utoronto.fitbook.entity.Exercise;
import ca.utoronto.fitbook.entity.RepetitiveExercise;
import ca.utoronto.fitbook.entity.TemporalExercise;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ViewExerciseInfoService implements ViewExerciseInfoUseCase {

    private final LoadAllExercisesPort loadAllExercisesPort;

    @Override
    public ViewExerciseInfoResponse viewInfo() {
        // Create list of all database exercises
        List<Exercise> exerciseList = loadAllExercisesPort.loadAllExercises();

        // Create new blank lists for the two types of exercises
        ArrayList<TemporalExercise> temporalExercises = new ArrayList<>();
        ArrayList<RepetitiveExercise> repetitiveExercises = new ArrayList<>();

        for (Exercise exercise : exerciseList) {
            if (exercise instanceof TemporalExercise){
                temporalExercises.add((TemporalExercise) exercise);
            } else if (exercise instanceof RepetitiveExercise){
                repetitiveExercises.add((RepetitiveExercise) exercise);
            } else {
                throw new InvalidExerciseTypeException(exercise.getClass().getName());
            }
        }
        return new ViewExerciseInfoResponse(temporalExercises, repetitiveExercises);
    }
}
