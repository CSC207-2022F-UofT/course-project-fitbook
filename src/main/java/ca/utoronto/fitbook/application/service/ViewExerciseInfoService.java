package ca.utoronto.fitbook.application.service;


import ca.utoronto.fitbook.application.port.in.LoadAllExercisesPort;
import ca.utoronto.fitbook.application.port.in.ViewExerciseInfoUseCase;
import ca.utoronto.fitbook.application.port.out.response.ViewExerciseInfoResponse;
import ca.utoronto.fitbook.entity.Exercise;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ViewExerciseInfoService implements ViewExerciseInfoUseCase {

    private final LoadAllExercisesPort loadAllExercisesPort;

    @Override
    public ViewExerciseInfoResponse viewInfo() {
        ArrayList<Exercise> exerciseList = loadAllExercisesPort.loadAllExercises();

        return new ViewExerciseInfoResponse(exerciseList);
    }
}
