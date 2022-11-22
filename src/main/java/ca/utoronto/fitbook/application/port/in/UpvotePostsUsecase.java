package ca.utoronto.fitbook.application.port.in;

import ca.utoronto.fitbook.application.port.in.command.UpvotePostsCommand;
import ca.utoronto.fitbook.application.port.out.response.UpvotePostsResponse;
import ca.utoronto.fitbook.entity.User;

public interface UpvotePostsUsecase {
    UpvotePostsResponse upvotePost(UpvotePostsCommand command);

}
