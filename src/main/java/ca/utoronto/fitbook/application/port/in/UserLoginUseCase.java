package ca.utoronto.fitbook.application.port.in;

import ca.utoronto.fitbook.application.port.in.command.UserLoginCommand;
import ca.utoronto.fitbook.application.port.out.response.UserLoginResponse;

public interface UserLoginUseCase {
    UserLoginResponse loginUser(UserLoginCommand command);
}
