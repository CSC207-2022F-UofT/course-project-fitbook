package ca.utoronto.fitbook.application.service;

import ca.utoronto.fitbook.application.exceptions.IncorrectPasswordException;
import ca.utoronto.fitbook.application.exceptions.UserNotFoundException;
import ca.utoronto.fitbook.application.port.in.FindUserByNamePort;
import ca.utoronto.fitbook.application.port.in.LoadUserByNamePort;
import ca.utoronto.fitbook.application.port.in.UserLoginUseCase;
import ca.utoronto.fitbook.application.port.in.command.UserLoginCommand;
import ca.utoronto.fitbook.application.port.out.response.UserLoginResponse;
import ca.utoronto.fitbook.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
@Service
@RequiredArgsConstructor
public class UserLoginService implements UserLoginUseCase {
    private final LoadUserByNamePort loadUserByNamePort;
    private final FindUserByNamePort findUserByNamePort;

    /**
     * @param command The username and password coming in from the user
     * @return the user id
     */
    @Override
    public UserLoginResponse loginUser(UserLoginCommand command) {
        if(!findUserByNamePort.findByName(command.getName()))
            throw new UserNotFoundException();

        User user = loadUserByNamePort.loadUserByName(command.getName());
        if (!user.getPassword().equals(command.getPassword()))
            throw new IncorrectPasswordException();

        return new UserLoginResponse(user.getId());
    }
}
