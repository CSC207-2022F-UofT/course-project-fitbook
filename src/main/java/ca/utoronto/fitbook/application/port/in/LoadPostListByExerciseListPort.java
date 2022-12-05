package ca.utoronto.fitbook.application.port.in;

import ca.utoronto.fitbook.entity.Post;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface LoadPostListByExerciseListPort {
    List<Post> loadPostListByExerciseList(List<String> exerciseIdList) throws InterruptedException, ExecutionException;
}
