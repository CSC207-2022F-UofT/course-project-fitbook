package ca.utoronto.fitbook.integration;

import ca.utoronto.fitbook.TestUtilities;
import ca.utoronto.fitbook.adapter.persistence.firebase.UserFirebaseRepository;
import ca.utoronto.fitbook.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserRegisterControllerIntegrationTest extends ControllerBaseIntegrationTest {
    @Autowired
    private UserFirebaseRepository userFirebaseRepository;
    // Making a post request to register with valid params and expecting it to register the user and return homePage

    @Test
    public void successfullyRegisterAUser() throws Exception {
        // Generating a user and creating a session instance
        User randomUser = TestUtilities.randomUser();
        MockHttpSession session = new MockHttpSession();
        this.mockMvc.perform(post("/register")
                        .queryParam("name", randomUser.getName())
                        .queryParam("password", randomUser.getPassword())
                        .queryParam("repeatedPassword", randomUser.getPassword())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .session(session))
                .andExpect(status().isFound());
        // Confirming the user ids are the same
        User databaseUser = userFirebaseRepository.loadUserByName(randomUser.getName());
        Assertions.assertEquals(databaseUser.getId(), session.getAttribute("userId"));
        userFirebaseRepository.delete(databaseUser.getId());
    }
    // Making a post request to register with mismatch passwords and expecting it to return a client error
    @Test
    public void failUserRegisterForPasswordMismatch() throws Exception {
        // Creating a session instance
        MockHttpSession session = new MockHttpSession();
        this.mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .queryParam("name", "modaserr")
                        .queryParam("password", "modaser12")
                        .queryParam("repeatedPassword", "modaser123")
                        .session(session))
                .andExpect(status().isBadRequest());
    }
    // Making a post request to register with too short passwords and expecting it to return a client error
    @Test
    public void failUserRegisterForShortPasswords() throws Exception {
        // Creating a session instance
        MockHttpSession session = new MockHttpSession();
        this.mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .queryParam("name", "modaserr")
                        .queryParam("password", "12")
                        .queryParam("repeatedPassword", "12")
                        .session(session))
                .andExpect(status().isUnprocessableEntity());
    }
    // Making a post request to register with too long passwords and expecting it to return a client error
    @Test
    public void failUserRegisterForLongPasswords() throws Exception {
        // Creating a session instance
        MockHttpSession session = new MockHttpSession();
        this.mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .queryParam("name", "modaserr")
                        .queryParam("password", "123412341234123412341234123412341234123412341234")
                        .queryParam("repeatedPassword", "123412341234123412341234123412341234123412341234")
                        .session(session))
                .andExpect(status().isUnprocessableEntity());
    }
    // Making a post request to register with too long username and expecting it to return a client error
    @Test
    public void failUserRegisterForLongUsername() throws Exception {
        // Creating a session instance
        MockHttpSession session = new MockHttpSession();
        this.mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .queryParam("name", "modaser123modaser123modaser123modaser123m")
                        .queryParam("password", "123123123")
                        .queryParam("repeatedPassword", "123123123")
                        .session(session))
                .andExpect(status().isUnprocessableEntity());
    }
    // Making a post request to register with too long username and expecting it to return a client error
    @Test
    public void failUserRegisterForShortUsername() throws Exception {
        // Creating a session instance
        MockHttpSession session = new MockHttpSession();
        this.mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .queryParam("name", "mo")
                        .queryParam("password", "123123123")
                        .queryParam("repeatedPassword", "123123123")
                        .session(session))
                .andExpect(status().isUnprocessableEntity());
    }
    // Making a post request to register with an existing username and expecting it to return a client error
    @Test
    public void failUserRegisterForUsernameAlreadyExists() throws Exception {
        // Generating a user, saving it to database and creating a session instance
        MockHttpSession session = new MockHttpSession();
        User existUser = TestUtilities.randomUser();
        userFirebaseRepository.save(existUser);
        this.mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .queryParam("name", existUser.getName())
                        .queryParam("password", "123123123")
                        .queryParam("repeatedPassword", "123123123")
                        .session(session))
                .andExpect(status().isConflict());

        userFirebaseRepository.delete(existUser.getId());
    }
    // Making a get request to register and expecting the register page
    @Test
    public void successfullyLoadTheRegisterPage() throws Exception {
        this.mockMvc.perform(get("/register"))
                .andExpect(status().isOk()).
                andExpect(content().contentType("text/html;charset=UTF-8"));
    }
}
