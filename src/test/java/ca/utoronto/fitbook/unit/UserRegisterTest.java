package ca.utoronto.fitbook.unit;

import ca.utoronto.fitbook.BaseTest;
import ca.utoronto.fitbook.adapter.persistence.localmemory.UserLocalMemoryRepository;
import ca.utoronto.fitbook.application.exceptions.UsernameAlreadyExistsException;
import ca.utoronto.fitbook.application.port.in.UserRegisterUseCase;
import ca.utoronto.fitbook.application.port.in.command.UserProfileCommand;
import ca.utoronto.fitbook.application.port.in.command.UserRegisterCommand;
import ca.utoronto.fitbook.application.port.out.response.UserRegisterResponse;
import ca.utoronto.fitbook.application.service.UserRegisterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

public class UserRegisterTest extends BaseTest {
    // The register use case instance
    private UserRegisterUseCase userRegisterUseCase;
    // Local repo for testing
    private UserLocalMemoryRepository userLocalMemoryRepository;
    // The register commands that will be sent to the use case
    // The id of a user that will be created stored so it can be deleted after testing
    private String hanaId;

    @BeforeAll
    public void init() {
        // Initializing the local repo
        this.userLocalMemoryRepository = new UserLocalMemoryRepository();
        // Initializing the use case
        this.userRegisterUseCase = new UserRegisterService(userLocalMemoryRepository, userLocalMemoryRepository);
    }

    // Testing when the two passwords don't match
    @Test
    public void testPasswordNotMatch() {
        UserRegisterCommand jhon = new UserRegisterCommand("jhon", "12345678", "123456789");
        Assertions.assertThrows(UserRegisterService.PasswordNotMatchException.class, () -> userRegisterUseCase.createUser(jhon));
    }

    // Testing when the password is smaller than 8 characters
    @Test
    public void testPasswordTooShort() {
        UserRegisterCommand henny = new UserRegisterCommand("henny", "123", "123");
        Assertions.assertThrows(UserRegisterService.PasswordTooShortException.class, () -> userRegisterUseCase.createUser(henny));
    }

    // Testing when the password is bigger than 40 characters
    @Test
    public void testPasswordTooLong() {
        UserRegisterCommand cat = new UserRegisterCommand("cat", "12345678901234567890123456789012345678901", "12345678901234567890123456789012345678901");
        Assertions.assertThrows(UserRegisterService.PasswordTooLongException.class, () -> userRegisterUseCase.createUser(cat));
    }

    // Testing when the username is smaller than 3 characters
    @Test
    public void testUsernameTooShort() {
        UserRegisterCommand tom = new UserRegisterCommand("", "1234567", "1234567");
        Assertions.assertThrows(UserRegisterService.NameTooShortException.class, () -> userRegisterUseCase.createUser(tom));
    }

    // Testing when the username is bigger than 40 characters
    @Test
    public void testUsernameTooLong() {
        UserRegisterCommand bill = new UserRegisterCommand("BillBillBiBillBillBiBillBillBiBillBillBil", "123456789", "123456789");
        Assertions.assertThrows(UserRegisterService.NameTooLongException.class, () -> userRegisterUseCase.createUser(bill));
    }

    // Testing that a user is created if the username and passwords are correct
    @Test
    public void testCreateUser() {
        UserRegisterCommand hana = new UserRegisterCommand("hana", "123456789", "123456789");
        hanaId = userRegisterUseCase.createUser(hana).getId();
        Assertions.assertInstanceOf(String.class, hanaId);
        userLocalMemoryRepository.delete(hanaId);
    }

    // Testing when the username already exists
    @Test
    public void testUsernameAlreadyExists() {
        UserRegisterCommand tana = new UserRegisterCommand("tana", "123456789", "123456789");
        userRegisterUseCase.createUser(tana);
        UserRegisterCommand tanaAgain = new UserRegisterCommand("tana", "123123123", "123123123");
        Assertions.assertThrows(UsernameAlreadyExistsException.class, () -> userRegisterUseCase.createUser(tanaAgain));
    }
}
