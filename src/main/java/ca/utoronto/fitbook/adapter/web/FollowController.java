package ca.utoronto.fitbook.adapter.web;

import ca.utoronto.fitbook.application.port.in.FollowUseCase;
import ca.utoronto.fitbook.application.port.in.command.FollowCommand;
import ca.utoronto.fitbook.application.port.out.response.FollowResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class FollowController
{
    private final FollowUseCase followUseCase;

    @PostMapping(path = "/follow")
    String followUser(Model model, @RequestBody FollowCommand command) {
        FollowResponse response = followUseCase.followUser(command);
        model.addAttribute("id", response.getFolloweeId());
        return "index";
    }
}

