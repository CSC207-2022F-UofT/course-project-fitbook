package ca.utoronto.fitbook.application.service;

import ca.utoronto.fitbook.application.port.in.LoadUserPort;
import ca.utoronto.fitbook.application.port.in.command.FollowCommand;
import ca.utoronto.fitbook.application.port.in.FollowUseCase;
import ca.utoronto.fitbook.application.port.out.SaveUserPort;
import ca.utoronto.fitbook.application.port.out.response.FollowResponse;
import ca.utoronto.fitbook.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

@Service
@RequiredArgsConstructor
public class FollowService implements FollowUseCase
{
    private final SaveUserPort saveUserPort;
    private final LoadUserPort loadUserPort;

    /**
     * @param command Object containing followerID and followeeID
     * @return Returns the ID of the followee
     */
    @Override
    public FollowResponse followUser(FollowCommand command) {

        User follower = loadUserPort.loadUser(command.getFollowerId());
        User followee = loadUserPort.loadUser(command.getFolloweeId());

        if (follower.getId().equals(followee.getId()))
            throw new UserSelfFollowingException();

        follower.getFollowingIdList().add(followee.getId());
        followee.getFollowersIdList().add(follower.getId());
        saveUserPort.saveUser(follower);
        saveUserPort.saveUser(followee);

        return new FollowResponse(followee.getId());
    }
    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="User tried to follow themself.")
    public static class UserSelfFollowingException extends RuntimeException {
        public UserSelfFollowingException() {
            super("User tried to follow themself.");
        }
    }
}

