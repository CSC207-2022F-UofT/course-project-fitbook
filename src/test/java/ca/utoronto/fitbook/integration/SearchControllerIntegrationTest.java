package ca.utoronto.fitbook.integration;

import ca.utoronto.fitbook.adapter.persistence.firebase.ExerciseFirebaseRepository;
import ca.utoronto.fitbook.adapter.persistence.firebase.PostFirebaseRepository;
import ca.utoronto.fitbook.adapter.persistence.firebase.UserFirebaseRepository;
import ca.utoronto.fitbook.entity.Exercise;
import ca.utoronto.fitbook.entity.Post;
import ca.utoronto.fitbook.entity.RepetitiveExercise;
import ca.utoronto.fitbook.entity.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class SearchControllerIntegrationTest extends ControllerBaseIntegrationTest {

    @Autowired
    private UserFirebaseRepository userFirebaseRepository;

    @Autowired
    private PostFirebaseRepository postFirebaseRepository;

    @Autowired
    private ExerciseFirebaseRepository exerciseFirebaseRepository;

    private User testUser;
    private Exercise exercise1;
    private Post post1;
    private MockHttpSession authorizedSession, unauthorizedSession;

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

        exercise1 = RepetitiveExercise.builder()
                .reps(5)
                .sets(2)
                .bodyParts(List.of("glute"))
                .name("squat")
                .keywords(List.of("glute", "squat"))
                .build();

        post1 = Post.builder()
                .postDate(new Date())
                .authorId(this.testUser.getId())
                .description("Best glute workout")
                .exerciseIdList(List.of(exercise1.getId()))
                .likes(2).build();

        testUser.setPostIdList(List.of(post1.getId()));

        exerciseFirebaseRepository.save(exercise1);
        postFirebaseRepository.save(post1);
        userFirebaseRepository.save(testUser);

        authorizedSession = new MockHttpSession();
        authorizedSession.setAttribute("userId", testUser.getId());
        unauthorizedSession = new MockHttpSession();
    }

    @AfterAll
    public void cleanUp() {
        postFirebaseRepository.delete(post1.getId());
        exerciseFirebaseRepository.delete(exercise1.getId());
        userFirebaseRepository.delete(testUser.getId());
    }

    @Test
    public void testUnauthorizedSearchAttempThrowUnauthorizedExcepion() throws Exception {
        this.mockMvc.perform(get("/search").session(unauthorizedSession).queryParam("queryString", "Best glute workout"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void searchPostsWithValidQueryReturnsValidContentAndAttributesTest() throws Exception {
        this.mockMvc.perform(get("/search").session(authorizedSession).queryParam("queryString", "Best glute workout"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("postList"))
                .andExpect(model().attributeExists("postAuthorNames"))
                .andExpect(model().attributeExists("exerciseListMap"));
    }

    @Test
    public void searchWithInvalidQueryThrowsEmptyQueryStringException() throws Exception {
        this.mockMvc.perform(get("/search").session(authorizedSession).queryParam("queryString", ""))
                .andExpect(status().isBadRequest());
    }

}
