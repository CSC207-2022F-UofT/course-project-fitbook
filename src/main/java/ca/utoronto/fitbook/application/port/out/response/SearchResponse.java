package ca.utoronto.fitbook.application.port.out.response;

import ca.utoronto.fitbook.entity.Post;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
public class SearchResponse {
    @NonNull
    List<Post> postList;
}
