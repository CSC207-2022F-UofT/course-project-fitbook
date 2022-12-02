package ca.utoronto.fitbook.tutorial;

import ca.utoronto.fitbook.adapter.persistence.localmemory.UserLocalMemoryRepository;
import ca.utoronto.fitbook.application.port.in.UserLoginUseCase;
import ca.utoronto.fitbook.application.port.in.command.UserLoginCommand;
import ca.utoronto.fitbook.application.port.in.command.UserRegisterCommand;
import ca.utoronto.fitbook.application.port.out.response.UserLoginResponse;
import ca.utoronto.fitbook.application.service.UserLoginService;
import ca.utoronto.fitbook.entity.User;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import java.util.ArrayList;
import java.util.Date;

public class UserLoginTest {
    // The login use case instance
    private UserLoginUseCase userLoginUseCase;
    // A user that will be used in testing
    private User testUser;
    // Local repo for testing
    private UserLocalMemoryRepository userLocalMemoryRepository;
    // the login commands that will be sent to the use case
    private UserLoginCommand jhon;
    private UserLoginCommand tom;
    private UserLoginCommand wrongJhon;

    @BeforeAll
    public void init(){
        // Initializing the local repo
        this.userLocalMemoryRepository = new UserLocalMemoryRepository();
        // creating the user and adding the user to the local repository
        testUser = User.builder()
                .followersIdList(new ArrayList<>())
                .joinDate(new Date())
                .name("jhon")
                .totalLikes(1)
                .password("123456789")
                .postIdList(new ArrayList<>())
                .followingIdList(new ArrayList<>())
                .followersIdList(new ArrayList<>())
                .likedPostIdList(new ArrayList<>())
                .build();
        userLocalMemoryRepository.save(testUser);
        // Creating the commands that will be used in testing
        jhon = new UserLoginCommand("jhon","123456789");
        wrongJhon = new UserLoginCommand("jhon","12345678");
        tom = new UserLoginCommand("tom","123456789");
    }

    @AfterAll
    public void cleanUp() {
        userLocalMemoryRepository.delete(testUser.getId());
    }
    // Testing when a correct username and password are entered
    @Test
    public void testUserLoggedIn(){
        UserLoginResponse response = userLoginUseCase.loginUser(jhon);
        Assertions.assertEquals(response.getId(),testUser.getId());
    }
    // Testing when an incorrect password is entered
    @Test
    public void testIncorrectPassword(){
    Assertions.assertThrows(UserLoginService.IncorrectPassword.class, () -> userLoginUseCase.loginUser(wrongJhon));
    }
    // Testing when the user does not exist
    @Test
    public void testUserDoNotExist(){
    Assertions.assertThrows(UserLoginService.UserNotFound.class, () -> userLoginUseCase.loginUser(tom));
    }
}
