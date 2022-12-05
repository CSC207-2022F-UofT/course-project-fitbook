package ca.utoronto.fitbook.integration;

import ca.utoronto.fitbook.TestUtilities;
import ca.utoronto.fitbook.adapter.persistence.firebase.UserFirebaseRepository;
import ca.utoronto.fitbook.entity.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserRegisterControllerIntegrationTest extends ControllerBaseIntegrationTest{

    @Autowired
    private UserFirebaseRepository userFirebaseRepository;
    private User testUser;
    private MockHttpSession session;
    @BeforeAll
    public void init() {
        // Initializing the instances
        testUser = TestUtilities.randomUser();

        userFirebaseRepository.save(testUser);


        session = new MockHttpSession();



    }



    @AfterAll
    public void cleanUp() {
        userFirebaseRepository.delete(testUser.getId());
    }

    // Making a post request to register with valid params and expecting it to register the user and return homePage
    @Test
    public void successfullyRegisterAUser() throws Exception {
        MvcResult result =  this.mockMvc.perform(post("/register")
                        .queryParam("name","modaser1")
                        .queryParam("password","modaser123")
                        .queryParam("repeatedPassword","modaser123")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8")).andReturn();

        userFirebaseRepository.loadUser((String) result.getModelAndView().getModel().get("id"));
        userFirebaseRepository.delete(userFirebaseRepository.loadUserByName("modaser1").getId());
    }

    // Making a post request to register with invalid params and expecting it to return a client error
    @Test
    public void failUserRegisterForWrongParams() throws Exception {
        MvcResult result =  this.mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .queryParam("name","modaserr")
                        .queryParam("password","modaser12")
                        .queryParam("repeatedPassword","modaser123")
                        .session(session))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    // Making a get request to register and expecting the register page
    @Test
    public void successfullyLoadTheRegisterPage() throws Exception {
        this.mockMvc.perform(get("/register"))
                .andExpect(status().isOk()).
                andExpect(content().contentType("text/html;charset=UTF-8"));
    }


}
