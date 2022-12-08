package ca.utoronto.fitbook.application.port.out.response;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
public class SearchResponse {
    @NonNull
    List<PostResponse> postList;
}
