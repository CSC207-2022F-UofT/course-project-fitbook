package ca.utoronto.fitbook.integration;

import ca.utoronto.fitbook.TestUtilities;
import ca.utoronto.fitbook.adapter.persistence.firebase.PostFirebaseRepository;
import ca.utoronto.fitbook.adapter.persistence.firebase.UserFirebaseRepository;
import ca.utoronto.fitbook.entity.Post;
import ca.utoronto.fitbook.entity.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PersonalizedFeedControllerIntegrationTest extends ControllerBaseIntegrationTest
{

    @Autowired
    private UserFirebaseRepository userFirebaseRepository;
    @Autowired
    private PostFirebaseRepository postFirebaseRepository;

    private List<Post> randomTestPosts;
    private List<User> randomTestUsers;

    private MockHttpSession authorizedSession, unauthorizedSession;

    @BeforeAll
    public void init() {
        randomTestPosts = new ArrayList<>();
        randomTestUsers = new ArrayList<>();

        // Create 20 random users and posts
        for (int i = 0; i < 20; i++) {
            User randomUser = TestUtilities.randomUser();
            Post randomPost = TestUtilities.randomPost(randomUser.getId());
            randomUser.getPostIdList().add(randomPost.getId());

            userFirebaseRepository.save(randomUser);
            randomTestUsers.add(randomUser);
            postFirebaseRepository.save(randomPost);
            randomTestPosts.add(randomPost);
        }

        authorizedSession = new MockHttpSession();
        authorizedSession.setAttribute("userId", randomTestUsers.get(0).getId());

        unauthorizedSession = new MockHttpSession();
    }

    @AfterAll
    public void cleanUp() {
        for (User user : randomTestUsers)
            userFirebaseRepository.delete(user.getId());
        for (Post post : randomTestPosts)
            postFirebaseRepository.delete(post.getId());
    }


    @Test
    public void testUnauthorizedSessionBlocked() throws Exception {
        this.mockMvc.perform(get("/feed").session(unauthorizedSession))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testFeedIsDisplayed() throws Exception {
        this.mockMvc.perform(get("/feed").session(authorizedSession))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attributeExists("paginationKey"));
    }

    @Test
    public void testFeedWithPaginationIsDisplayed() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/feed").session(authorizedSession)).andReturn();
        Assertions.assertNotNull(result.getModelAndView());
        String paginationKey = (String) result.getModelAndView().getModel().get("paginationKey");
        this.mockMvc.perform(get("/feed")
                        .queryParam("paginationKey", paginationKey)
                        .session(authorizedSession))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("posts"));
    }
}