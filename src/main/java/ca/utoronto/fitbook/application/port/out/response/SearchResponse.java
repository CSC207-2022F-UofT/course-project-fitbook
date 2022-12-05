package ca.utoronto.fitbook.application.port.out.response;

import ca.utoronto.fitbook.entity.Exercise;
import ca.utoronto.fitbook.entity.Post;
import lombok.NonNull;
import lombok.Value;

import java.util.HashMap;
import java.util.List;

@Value
public class SearchResponse {
    @NonNull
    List<Post> postList;

    @NonNull
    HashMap<String, Exercise> exerciseListMap;

    @NonNull
    HashMap<String, String> postAuthorNames;
}
