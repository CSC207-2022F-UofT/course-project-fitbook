package ca.utoronto.fitbook.application.port.in.command;

import lombok.NonNull;
import lombok.Value;

@Value
public class UpvotePostsCommand {

    @NonNull
    String postId;
    @NonNull
    String userId;
}
