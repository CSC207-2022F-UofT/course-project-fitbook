package ca.utoronto.fitbook.integration;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IndexControllerIntegrationTest extends ControllerBaseIntegrationTest
{
    @Test
    public void redirectLoggedOutUserTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        this.mockMvc.perform(get("/").session(session))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("login"));
    }

    @Test
    public void redirectLoggedInUserTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", "0000");
        this.mockMvc.perform(get("/").session(session))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("feed"));
    }
}