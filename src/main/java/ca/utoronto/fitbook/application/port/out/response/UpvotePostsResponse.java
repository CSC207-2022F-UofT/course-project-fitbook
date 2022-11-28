package ca.utoronto.fitbook.application.port.out.response;

import lombok.NonNull;
import lombok.Value;

@Value
public class UpvotePostsResponse {
    @NonNull
    String postLikerID;
    @NonNull
    String postAuthorID;
    int postLikes;
}
