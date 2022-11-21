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
     * @param command
     * @return
     */
    @Override
    public UserRegisterResponse createUser(UserRegisterCommand command){
        if (findUserByNamePort.findByName(command.getName())) {
            throw  new UsernameAlreadyExists();
        } else if (!command.getPassword().equals(command.getRepeatedPassword())) {
            throw new PasswordNotMatch();
        }else if (command.getName().length()>40){
            throw new NameTooLong(command);
        }else if (command.getPassword().length()<8){
            throw new PasswordTooShort();
        }else if (command.getPassword().length()>40){
            throw new PasswordTooLong(command);
        }
        var user = User.builder()
                .likedPostIdList(new ArrayList<>())
                .followingIdList(new ArrayList<>())
                .followersIdList(new ArrayList<>())
                .postIdList(new ArrayList<>())
                .name(command.getName())
                .password(command.getPassword())
                .joinDate(new Date())
                .build();
        saveUserPort.save(user);
        UserRegisterResponse userRegisterResponse = new UserRegisterResponse(user.getId());
        return userRegisterResponse;
    }
    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Username already exists")
    public class UsernameAlreadyExists extends RuntimeException {
        public UsernameAlreadyExists() {
            super("Username already exists.");
        }
    }
    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Password don't match")
    public class PasswordNotMatch extends RuntimeException {
        public PasswordNotMatch() {
            super("Password don't match.");
        }
    }
    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Name is too long")
    public class NameTooLong extends RuntimeException {
        public NameTooLong(UserRegisterCommand command) {
            super("Name is too long by "+ ((command.getName().length())-40)+" characters");
        }
    }
    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Password is too long")
    public class PasswordTooLong extends RuntimeException {
        public PasswordTooLong(UserRegisterCommand command) {
            super("Password is too long by "+ ((command.getName().length())-40)+" characters");
        }
    }
    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Password is too short")
    public class PasswordTooShort extends RuntimeException {
        public PasswordTooShort() {
            super("Password is too short");
        }
    }

}
