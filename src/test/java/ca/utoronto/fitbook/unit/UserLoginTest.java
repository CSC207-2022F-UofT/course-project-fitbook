package ca.utoronto.fitbook.unit;

import ca.utoronto.fitbook.BaseTest;
import ca.utoronto.fitbook.TestUtilities;
import ca.utoronto.fitbook.adapter.persistence.localmemory.UserLocalMemoryRepository;
import ca.utoronto.fitbook.application.exceptions.IncorrectPasswordException;
import ca.utoronto.fitbook.application.exceptions.UsernameCollisionException;
import ca.utoronto.fitbook.application.exceptions.UsernameNotFoundException;
import ca.utoronto.fitbook.application.port.in.UserLoginUseCase;
import ca.utoronto.fitbook.application.port.in.command.UserLoginCommand;
import ca.utoronto.fitbook.application.port.out.response.UserLoginResponse;
import ca.utoronto.fitbook.application.service.UserLoginService;
import ca.utoronto.fitbook.entity.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UserLoginTest extends BaseTest {
    // The login use case instance
    private UserLoginUseCase userLoginUseCase;
    // A user that will be used in testing
    private User testUser;
    // Local repo for testing
    private UserLocalMemoryRepository userLocalMemoryRepository;

    @BeforeAll
    public void init() {

        // initializing the local repo
        this.userLocalMemoryRepository = new UserLocalMemoryRepository();

        // initializing the use case
        userLoginUseCase = new UserLoginService(userLocalMemoryRepository);
        // creating the user and adding the user to the local repository

        testUser = TestUtilities.randomUser();
        userLocalMemoryRepository.save(testUser);
    }

    @AfterAll
    public void cleanUp() {
        userLocalMemoryRepository.delete(testUser.getId());
    }

    // Testing when a correct username and password are entered
    @Test
    public void testUserLoggedIn() {
        UserLoginCommand command = new UserLoginCommand(testUser.getName(), testUser.getPassword());
        UserLoginResponse response = userLoginUseCase.loginUser(command);
        Assertions.assertEquals(response.getId(), testUser.getId());
    }

    // Testing when an incorrect password is entered
    @Test
    public void testIncorrectPassword() {
        UserLoginCommand command = new UserLoginCommand(testUser.getName(), testUser.getPassword() + "a");
        Assertions.assertThrows(IncorrectPasswordException.class, () -> userLoginUseCase.loginUser(command));
    }

    // Testing when the user does not exist
    @Test
    public void testUserDoesNotExist() {
        UserLoginCommand command = new UserLoginCommand(testUser.getName() + "a", "123456789");
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userLoginUseCase.loginUser(command));
    }

    @Test
    public void testUsernameCollision() {
        User collidingUser = TestUtilities.randomUser();
        collidingUser.setName(testUser.getName());
        userLocalMemoryRepository.save(collidingUser);
        UserLoginCommand command = new UserLoginCommand(testUser.getName(), testUser.getPassword());
        Assertions.assertThrows(UsernameCollisionException.class, () -> userLoginUseCase.loginUser(command));
        userLocalMemoryRepository.delete(collidingUser.getId());
    }
}
