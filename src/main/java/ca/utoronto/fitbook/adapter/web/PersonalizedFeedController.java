package ca.utoronto.fitbook.adapter.web;

import ca.utoronto.fitbook.application.port.in.PersonalizedFeedUseCase;
import ca.utoronto.fitbook.application.port.in.command.PersonalizedFeedCommand;
import ca.utoronto.fitbook.application.port.out.response.PersonalizedFeedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class PersonalizedFeedController
{
    private final PersonalizedFeedUseCase personalizedFeedUseCase;

    @GetMapping(path = "/feed")
    String generateFeed(Model model, HttpSession session, @RequestParam(required = false) String paginationKey) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null)
            throw new UnauthorizedUserException();
        PersonalizedFeedCommand command = new PersonalizedFeedCommand(userId, paginationKey, 10);
        PersonalizedFeedResponse response = personalizedFeedUseCase.getFeed(command);
        model.addAttribute("posts", response.getPostList());
        model.addAttribute("paginationKey", response.getNextPaginationKey());
        return "feed";
    }
}
