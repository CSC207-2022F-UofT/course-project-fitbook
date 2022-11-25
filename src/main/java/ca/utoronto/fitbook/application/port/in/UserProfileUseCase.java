package ca.utoronto.fitbook.application.port.in;

import ca.utoronto.fitbook.application.port.in.command.UserProfileCommand;
import ca.utoronto.fitbook.application.port.out.response.UserProfileResponse;

public interface UserProfileUseCase {
    UserProfileResponse createProfile(UserProfileCommand userProfileCommand);
}
