package ca.utoronto.fitbook.application.service;


import ca.utoronto.fitbook.application.port.in.LoadAllExercisesPort;
import ca.utoronto.fitbook.application.port.in.ViewExerciseInfoUseCase;
import ca.utoronto.fitbook.application.port.out.response.ViewExerciseInfoResponse;
import ca.utoronto.fitbook.entity.Exercise;
import ca.utoronto.fitbook.entity.RepetitiveExercise;
import ca.utoronto.fitbook.entity.TemporalExercise;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ViewExerciseInfoService implements ViewExerciseInfoUseCase {

    private final LoadAllExercisesPort loadAllExercisesPort;

    @Override
    public ViewExerciseInfoResponse viewInfo() {
        // Create list of all database exercises
        ArrayList<Exercise> exerciseList = loadAllExercisesPort.loadAllExercises();

        // Create new blank lists for the two types of exercises
        ArrayList<TemporalExercise> temporalExercises = new ArrayList<>();
        ArrayList<RepetitiveExercise> repetitiveExercises = new ArrayList<>();

        for (Exercise exer : exerciseList) {
            if (exer instanceof TemporalExercise){
                temporalExercises.add((TemporalExercise) exer);
            } else {
                repetitiveExercises.add((RepetitiveExercise) exer);
            }
        }
        return new ViewExerciseInfoResponse(temporalExercises, repetitiveExercises);
    }
}