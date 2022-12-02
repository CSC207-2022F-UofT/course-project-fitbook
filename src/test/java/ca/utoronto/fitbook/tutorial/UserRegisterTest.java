package ca.utoronto.fitbook.tutorial;

import ca.utoronto.fitbook.adapter.persistence.localmemory.UserLocalMemoryRepository;
import ca.utoronto.fitbook.application.port.in.UserRegisterUseCase;
import ca.utoronto.fitbook.application.port.in.command.UserProfileCommand;
import ca.utoronto.fitbook.application.port.in.command.UserRegisterCommand;
import ca.utoronto.fitbook.application.port.out.response.UserRegisterResponse;
import ca.utoronto.fitbook.application.service.UserRegisterService;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

public class UserRegisterTest {
    // The register use case instance
    private UserRegisterUseCase userRegisterUseCase;
    // Local repo for testing
    private UserLocalMemoryRepository userLocalMemoryRepository;
    // The register commands that will be sent to the use case
    private UserRegisterCommand jhon;
    private UserRegisterCommand tom;
    private UserRegisterCommand henny;
    private UserRegisterCommand cat;
    private UserRegisterCommand hana;
    private UserRegisterCommand hanaAgain;
    private UserRegisterCommand bill;
    // The id of a user that will be created stored so it can be deleted after testing
    private String hanaId;

    @BeforeAll
    public void init(){
         // Initializing the local repo
         this.userLocalMemoryRepository = new UserLocalMemoryRepository();
         // Creating the commands that will be used in testing
         jhon = new UserRegisterCommand("jhon","12345678","123456789");
         tom = new UserRegisterCommand("","123456","1234567");
         henny = new UserRegisterCommand("henny","123","123");
         cat = new UserRegisterCommand("cat","12345678901234567890123456789012345678901","12345678901234567890123456789012345678901");
         hana = new UserRegisterCommand("hana","123456789","123456789");
         bill = new UserRegisterCommand("BillBillBiBillBillBiBillBillBiBillBillBil","123456789","123456789");
         hanaAgain = new UserRegisterCommand("hana","123123123","123123123");
    }

    @AfterAll
    public void cleanUp(){
        userLocalMemoryRepository.delete(hanaId);
    }

    // Testing when the two passwords don't match
    @Test
    public void testPasswordNotMatch(){
        Assertions.assertThrows(UserRegisterService.PasswordNotMatch.class, () -> userRegisterUseCase.createUser(jhon));
    }
    // Testing when the password is smaller than 8 characters
    @Test
    public void testPasswordTooShort(){
        Assertions.assertThrows(UserRegisterService.PasswordTooShort.class, () -> userRegisterUseCase.createUser(henny));
    }
    // Testing when the password is bigger than 40 characters
    @Test
    public void testPasswordTooLong(){
        Assertions.assertThrows(UserRegisterService.PasswordTooLong.class, () -> userRegisterUseCase.createUser(cat));
    }
    // Testing when the username is smaller than 3 characters
    @Test
    public void testUsernameTooShort(){
        Assertions.assertThrows(UserRegisterService.NameTooShort.class, () -> userRegisterUseCase.createUser(tom));
    }
    // Testing when the username is bigger than 40 characters
    @Test
    public void testUsernameTooLong(){
        Assertions.assertThrows(UserRegisterService.NameTooLong.class, () -> userRegisterUseCase.createUser(bill));
    }
    // Testing that a user is created if the username and passwords are correct
    @Test
    public void testCreateUser(){
        hanaId = userRegisterUseCase.createUser(hana).getId();
        Assertions.assertInstanceOf(String.class, hanaId);
    }
    // Testing when the username already exists
    @Test
    public void testUsernameAlreadyExists(){
        Assertions.assertThrows(UserRegisterService.UsernameAlreadyExists.class, () -> userRegisterUseCase.createUser(hanaAgain));
    }
}
