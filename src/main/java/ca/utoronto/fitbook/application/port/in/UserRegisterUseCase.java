package ca.utoronto.fitbook.application.port.in;

import ca.utoronto.fitbook.application.port.in.command.UserRegisterCommand;
import ca.utoronto.fitbook.application.port.out.response.UserRegisterResponse;

public interface UserRegisterUseCase {
    UserRegisterResponse createUser(UserRegisterCommand command);

}
