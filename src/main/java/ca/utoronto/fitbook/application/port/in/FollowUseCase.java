package ca.utoronto.fitbook.application.port.in;


import ca.utoronto.fitbook.application.port.in.command.FollowCommand;
import ca.utoronto.fitbook.application.port.out.response.FollowResponse;

public interface FollowUseCase
{
    FollowResponse followUser(FollowCommand command);
}
