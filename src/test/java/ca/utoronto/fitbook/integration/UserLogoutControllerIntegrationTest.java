package ca.utoronto.fitbook.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserLogoutControllerIntegrationTest extends ControllerBaseIntegrationTest
{
    @Test
    public void successfullyLogUserOutTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", "00000");
        this.mockMvc.perform(get("/logout").session(session))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("login"));

        Assertions.assertTrue(session.isInvalid());
    }
}


