package ca.utoronto.fitbook.adapter.web;

import ca.utoronto.fitbook.application.port.in.command.UpvotePostsCommand;
import ca.utoronto.fitbook.application.port.in.UpvotePostsUsecase;
import ca.utoronto.fitbook.application.port.out.response.UpvotePostsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class UpvotePostsController {

    private final UpvotePostsUsecase upvotePostsUsecase;

    @PostMapping(path = "/upvote")
    String upvotePosts(Model model, @RequestBody UpvotePostsCommand command) {
        UpvotePostsResponse response = UpvotePostsUsecase.upvotePost(command);
        model.addAttribute("likes", response.getLikes());
        return "likes";
    }
}
