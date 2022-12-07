package ca.utoronto.fitbook.integration;

import ca.utoronto.fitbook.TestUtilities;
import ca.utoronto.fitbook.adapter.persistence.firebase.UserFirebaseRepository;
import ca.utoronto.fitbook.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserLoginControllerIntegrationTest extends ControllerBaseIntegrationTest {
    @Autowired
    private UserFirebaseRepository userFirebaseRepository;

    // Making a post request to login with valid params and expecting it to return userId with homePage
    @Test
    public void successfullyLogTheUserIn() throws Exception {
        // Creating a user and saving to database
        User testUser = TestUtilities.randomUser();
        userFirebaseRepository.save(testUser);
        MockHttpSession session = new MockHttpSession();
        this.mockMvc.perform(post("/login")
                        .queryParam("name", testUser.getName())
                        .queryParam("password", testUser.getPassword())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .session(session))
                .andExpect(status().isFound());
        Assertions.assertEquals(testUser.getId(), session.getAttribute("userId"));

        Assertions.assertEquals(testUser.getId(), (String) session.getAttribute("userId"));
    }
    // Making a post request to login with wrong password and expecting it to return client error
    @Test
    public void failToLogUserForWrongPassword() throws Exception {
        // Creating a user and saving to database
        User testUser = TestUtilities.randomUser();
        userFirebaseRepository.save(testUser);
        MockHttpSession session = new MockHttpSession();
        this.mockMvc.perform(post("/login")
                        .queryParam("name", testUser.getName())
                        .queryParam("password", "wrongpassword")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .session(session))
                .andExpect(status().is4xxClientError());

    }
    // Making a post request to login with a username that does not exist and expecting it to return client error
    @Test
    public void failToLogUserForUsernameDoesNotExist() throws Exception {
        MockHttpSession session = new MockHttpSession();
        this.mockMvc.perform(post("/login")
                        .queryParam("name", "thisnamedoesnotexist")
                        .queryParam("password", "doesnotmatter")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .session(session))
                .andExpect(status().is4xxClientError());

    }
    // Making a get request to login and expecting the login page returned
    @Test
    public void successfullyLoadTheLoginpage() throws Exception {
        this.mockMvc.perform(get("/login"))
                .andExpect(status().isOk()).
                andExpect(content().contentType("text/html;charset=UTF-8"));
    }
}


