package ca.utoronto.fitbook.integration;
import ca.utoronto.fitbook.adapter.persistence.firebase.UserFirebaseRepository;
import ca.utoronto.fitbook.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class FollowControllerIntegrationTest extends ControllerBaseIntegrationTest {

    @Autowired
    private UserFirebaseRepository userFirebaseRepository;
    private User testUser1;
    private User testUser2;

    private MockHttpSession authorizedSession;
    private MockHttpSession unauthorizedSession;

    @BeforeAll
    public void init() {
        testUser1 = User.builder()
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

        testUser2 = User.builder()
                .followersIdList(new ArrayList<>())
                .joinDate(new Date())
                .name("Feb")
                .totalLikes(1)
                .password("pw2")
                .postIdList(new ArrayList<>())
                .followingIdList(new ArrayList<>())
                .followersIdList(new ArrayList<>())
                .likedPostIdList(new ArrayList<>())
                .build();

        userFirebaseRepository.save(testUser1);
        userFirebaseRepository.save(testUser2);

        authorizedSession = new MockHttpSession();
        authorizedSession.setAttribute("userId", this.testUser1.getId());
        unauthorizedSession = new MockHttpSession();
    }

    @AfterAll
    public void cleanUp() {
        userFirebaseRepository.delete(testUser1.getId());
        userFirebaseRepository.delete(testUser2.getId());
    }

    @AfterEach
    public void resetFollowers(){
        testUser1.setFollowersIdList(new ArrayList<>());
        testUser1.setFollowingIdList(new ArrayList<>());
        testUser2.setFollowersIdList(new ArrayList<>());
        testUser2.setFollowingIdList(new ArrayList<>());
        userFirebaseRepository.save(testUser1);
        userFirebaseRepository.save(testUser2);
    }

    @Test
    public void followUserUnauthorizedExpectsUnauthorizedResponse() throws Exception {
        Map<String, String> payload = new HashMap<>();
        payload.put("followerId", this.testUser1.getId());
        payload.put("followeeId", this.testUser2.getId());
        ObjectMapper mapper = new ObjectMapper();

        this.mockMvc.perform(post("/follow").session(unauthorizedSession).content(mapper.writeValueAsString(payload))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void followUserExpectsOkResponse() throws Exception {
        Map<String, String> payload = new HashMap<>();
        payload.put("followerId", this.testUser1.getId());
        payload.put("followeeId", this.testUser2.getId());
        ObjectMapper mapper = new ObjectMapper();

        this.mockMvc.perform(post("/follow").session(authorizedSession).content(mapper.writeValueAsString(payload))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    public void userFollowThemselvesExpectBadResponse() throws Exception {
        Map<String, String> payload = new HashMap<>();
        payload.put("followerId", this.testUser1.getId());
        payload.put("followeeId", this.testUser1.getId());
        ObjectMapper mapper = new ObjectMapper();

        this.mockMvc.perform(post("/follow").session(authorizedSession).content(mapper.writeValueAsString(payload))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void followUserFollowsAlreadyFollowingExceptsBadRequestResponse() throws Exception {
        testUser1.setFollowingIdList(List.of(testUser2.getId()));
        testUser2.setFollowersIdList(List.of(testUser1.getId()));
        userFirebaseRepository.save(testUser1);
        userFirebaseRepository.save(testUser2);

        Map<String, String> payload = new HashMap<>();
        payload.put("followerId", this.testUser1.getId());
        payload.put("followeeId", this.testUser2.getId());
        ObjectMapper mapper = new ObjectMapper();

        this.mockMvc.perform(post("/follow").session(authorizedSession).content(mapper.writeValueAsString(payload))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }
}