package ca.utoronto.fitbook.adapter.web;

import ca.utoronto.fitbook.adapter.web.requestbody.FollowRequestBody;
import ca.utoronto.fitbook.application.port.in.FollowUseCase;
import ca.utoronto.fitbook.application.port.in.command.FollowCommand;
import ca.utoronto.fitbook.application.port.out.response.FollowResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
public class FollowController
{
    private final FollowUseCase followUseCase;

    @PostMapping(path = "/follow")
    FollowResponse followUser(HttpSession session, @RequestBody FollowRequestBody body) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null)
            throw new UnauthorizedUserException();
        FollowCommand command = new FollowCommand(userId, body.getFolloweeId());

        return followUseCase.followUser(command);
    }
}

