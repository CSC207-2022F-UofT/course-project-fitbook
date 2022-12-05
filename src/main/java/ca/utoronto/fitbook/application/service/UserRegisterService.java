package ca.utoronto.fitbook.application.service;

import ca.utoronto.fitbook.application.port.in.FindUserByNamePort;
import ca.utoronto.fitbook.application.port.in.UserRegisterUseCase;
import ca.utoronto.fitbook.application.port.in.command.UserRegisterCommand;
import ca.utoronto.fitbook.application.port.out.SaveUserPort;
import ca.utoronto.fitbook.application.port.out.response.UserRegisterResponse;
import ca.utoronto.fitbook.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.ArrayList;
import java.util.Date;
@Service
@RequiredArgsConstructor
public class UserRegisterService implements UserRegisterUseCase {
    private final FindUserByNamePort findUserByNamePort;
    private final SaveUserPort saveUserPort;
    /**
     * @param command The username and password coming in from the user
     * @return the id for the new user created
     */
    @Override
    public UserRegisterResponse createUser(UserRegisterCommand command){
        // Verify the username isn't taken
        if (findUserByNamePort.findByName(command.getName()))
            throw new UsernameAlreadyExists();

        // Make sure passwords match
        if (!command.getPassword().equals(command.getRepeatedPassword()))
            throw new PasswordNotMatch();

        // Check username constraints
        if (command.getName().length() > 40)
            throw new NameTooLong(command);
        if (command.getName().length() < 3)
            throw new NameTooShort();

        // Check password constraints
        if (command.getPassword().length() < 8)
            throw new PasswordTooShort();
        if (command.getPassword().length() > 40)
            throw new PasswordTooLong(command);

        // Create and save the new user
        User user = User.builder()
                .likedPostIdList(new ArrayList<>())
                .followingIdList(new ArrayList<>())
                .followersIdList(new ArrayList<>())
                .postIdList(new ArrayList<>())
                .name(command.getName())
                .password(command.getPassword())
                .joinDate(new Date())
                .build();
        saveUserPort.saveUser(user);

        // Return the user's id
        return new UserRegisterResponse(user.getId());
    }

    // The error thrown given a username already in the database
    @ResponseStatus(value=HttpStatus.CONFLICT, reason="Username already exists")
    public static class UsernameAlreadyExists extends RuntimeException {
        public UsernameAlreadyExists() {
            super("Username already exists.");
        }
    }

    // The error thrown given two mismatch passwords
    @ResponseStatus(value=HttpStatus.UNAUTHORIZED, reason="Password don't match")
    public static class PasswordNotMatch extends RuntimeException {
        public PasswordNotMatch() {
            super("Password don't match.");
        }
    }

    // The error thrown given a longer username than 40 chars
    @ResponseStatus(value=HttpStatus.UNPROCESSABLE_ENTITY, reason="Name is too long")
    public static class NameTooLong extends RuntimeException {
        public NameTooLong(UserRegisterCommand command) {
            super("Name is too long by " + ((command.getName().length()) - 40) + " characters");
        }
    }

    // The error thrown given a username shorter than 3 chars
    @ResponseStatus(value=HttpStatus.UNPROCESSABLE_ENTITY, reason="Name is too short")
    public static class NameTooShort extends RuntimeException {
        public NameTooShort() {
            super("Name is too short");
        }
    }

    // The error thrown given a password longer than 40 chars
    @ResponseStatus(value=HttpStatus.UNPROCESSABLE_ENTITY, reason="Password is too long")
    public static class PasswordTooLong extends RuntimeException {
        public PasswordTooLong(UserRegisterCommand command) {
            super("Password is too long by " + ((command.getName().length()) - 40) + " characters");
        }
    }

    // The error thrown given a password smaller than 8 chars
    @ResponseStatus(value=HttpStatus.UNPROCESSABLE_ENTITY, reason="Password is too short")
    public static class PasswordTooShort extends RuntimeException {
        public PasswordTooShort() {
            super("Password is too short");
        }
    }

}
