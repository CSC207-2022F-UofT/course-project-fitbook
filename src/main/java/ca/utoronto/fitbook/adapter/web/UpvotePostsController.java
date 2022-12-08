package ca.utoronto.fitbook.adapter.web;

import ca.utoronto.fitbook.adapter.web.requestbody.UpvoteRequestBody;
import ca.utoronto.fitbook.application.port.in.UpvotePostsUsecase;
import ca.utoronto.fitbook.application.port.in.command.UpvotePostsCommand;
import ca.utoronto.fitbook.application.port.out.response.UpvotePostsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
public class UpvotePostsController
{

    private final UpvotePostsUsecase upvotePostsUsecase;

    @PostMapping(path = "/upvote")
    UpvotePostsResponse upvotePosts(HttpSession session, @RequestBody UpvoteRequestBody body) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null)
            throw new UnauthorizedUserException();
        UpvotePostsCommand command = new UpvotePostsCommand(body.getPostId(), userId);
        return upvotePostsUsecase.upvotePost(command);
    }
}
