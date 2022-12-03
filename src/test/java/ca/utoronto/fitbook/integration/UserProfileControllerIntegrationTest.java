package ca.utoronto.fitbook.integration;
import ca.utoronto.fitbook.adapter.persistence.firebase.UserFirebaseRepository;
import ca.utoronto.fitbook.entity.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;

import java.util.ArrayList;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserProfileControllerIntegrationTest extends ControllerBaseIntegrationTest {

    @Autowired
    private UserFirebaseRepository userFirebaseRepository;

    private User testUser;

    private MockHttpSession session;

    @BeforeAll
    public void init() {

        testUser = User.builder()
                .followersIdList(new ArrayList<>())
                .joinDate(new Date())
                .name("Jan")
                .totalLikes(1)
                .password("pw")
                .postIdList(new ArrayList<>())
                .followingIdList(new ArrayList<>())
                .followersIdList(new ArrayList<>())
                .likedPostIdList(new ArrayList<>())
                .build();

        userFirebaseRepository.save(testUser);

        session = new MockHttpSession();
        session.setAttribute("userId", testUser.getId());
    }

    @AfterAll
    public void cleanUp() {
        userFirebaseRepository.delete(testUser.getId());
    }


    @Test
    public void findExistingUserReturnsProfileWithValidContentAndAttributesTest() throws Exception {
        this.mockMvc.perform(get(String.format("/profile/%s", testUser.getId())).session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("profile"));
    }

    @Test
    public void findNonExistentUserProfileReturnsServerErrorHttpStatusTest() throws Exception {
        this.mockMvc.perform(get("/profile/-1").session(session))
                .andExpect(status().isUnprocessableEntity());
    }
}