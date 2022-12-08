package ca.utoronto.fitbook.integration;

import ca.utoronto.fitbook.TestUtilities;
import ca.utoronto.fitbook.adapter.persistence.firebase.PostFirebaseRepository;
import ca.utoronto.fitbook.adapter.persistence.firebase.UserFirebaseRepository;
import ca.utoronto.fitbook.adapter.web.requestbody.UpvoteRequestBody;
import ca.utoronto.fitbook.entity.Post;
import ca.utoronto.fitbook.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UpvotePostsControllerIntegrationTest extends ControllerBaseIntegrationTest
{

    @Autowired
    private UserFirebaseRepository userFirebaseRepository;
    @Autowired
    private PostFirebaseRepository postFirebaseRepository;

    private MockHttpSession authorizedSession;

    private User testPostLiker;
    private User testPostAuthor;
    private Post testPost;

    @BeforeEach
    public void initializeTest() {
        testPostLiker = TestUtilities.randomUser();
        testPostAuthor = TestUtilities.randomUser();

        testPost = TestUtilities.randomPost(testPostAuthor.getId());

        authorizedSession = new MockHttpSession();
        authorizedSession.setAttribute("userId", testPostLiker.getId());

        userFirebaseRepository.save(testPostLiker);
        userFirebaseRepository.save(testPostAuthor);
        postFirebaseRepository.save(testPost);
    }

    private void reloadTestEntities() {
        testPostLiker = userFirebaseRepository.loadUser(testPostLiker.getId());
        testPostAuthor = userFirebaseRepository.loadUser(testPostAuthor.getId());
        testPost = postFirebaseRepository.loadPost(testPost.getId());
    }

    private String generateRequestBody(UpvoteRequestBody body) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    public void cleanUp() {
        userFirebaseRepository.delete(testPostLiker.getId());
        userFirebaseRepository.delete(testPostAuthor.getId());
        postFirebaseRepository.delete(testPost.getId());
    }


    @Test
    public void testUnauthorizedSessionBlocked() throws Exception {
        UpvoteRequestBody body = new UpvoteRequestBody();
        body.setPostId(testPost.getId());
        MockHttpSession unauthorizedSession = new MockHttpSession();
        this.mockMvc.perform(post("/upvote").session(unauthorizedSession)
                        .content(generateRequestBody(body))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testUpvoteIsSuccessful() throws Exception {
        int previousPostLikes = testPost.getLikes();
        int previousAuthorLikes = testPostAuthor.getTotalLikes();
        UpvoteRequestBody body = new UpvoteRequestBody();
        body.setPostId(testPost.getId());

        this.mockMvc.perform(post("/upvote").session(authorizedSession)
                        .content(generateRequestBody(body))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
        reloadTestEntities();

        Assertions.assertTrue(testPostLiker.getLikedPostIdList().contains(testPost.getId()));
        Assertions.assertEquals(previousPostLikes + 1, testPost.getLikes());
        Assertions.assertEquals(previousAuthorLikes + 1, testPostAuthor.getTotalLikes());
    }
}