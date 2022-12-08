package ca.utoronto.fitbook.application.port.in;

import ca.utoronto.fitbook.entity.Post;

import java.util.List;

public interface LoadPostListByExerciseListPort {
    List<Post> loadPostListByExerciseList(List<String> exerciseIdList);
}
