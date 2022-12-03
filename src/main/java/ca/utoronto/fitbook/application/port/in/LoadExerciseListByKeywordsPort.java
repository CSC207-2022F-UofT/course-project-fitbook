package ca.utoronto.fitbook.application.port.in;

import ca.utoronto.fitbook.entity.Exercise;
import ca.utoronto.fitbook.entity.Post;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface LoadExerciseListByKeywordsPort {

    List<Exercise> loadExerciseListByKeywords(List<String> keywords);

}
