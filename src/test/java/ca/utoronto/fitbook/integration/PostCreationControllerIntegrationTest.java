package ca.utoronto.fitbook.integration;

import ca.utoronto.fitbook.adapter.persistence.firebase.ExerciseFirebaseRepository;
import ca.utoronto.fitbook.adapter.persistence.firebase.PostFirebaseRepository;
import ca.utoronto.fitbook.adapter.persistence.firebase.UserFirebaseRepository;
import ca.utoronto.fitbook.entity.Exercise;
import ca.utoronto.fitbook.entity.TemporalExercise;
import ca.utoronto.fitbook.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostCreationControllerIntegrationTest extends ControllerBaseIntegrationTest{

    @Autowired
    private UserFirebaseRepository userFirebaseRepository;

    @Autowired
    private PostFirebaseRepository postFirebaseRepository;

    @Autowired
    private ExerciseFirebaseRepository exerciseFirebaseRepository;

    private User testUser;

    private Exercise testExercise;

    private MockHttpSession authorizedSession, unauthorizedSession;

    @BeforeAll
    public void init(){

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

        authorizedSession = new MockHttpSession();
        authorizedSession.setAttribute("userId", testUser.getId());

        unauthorizedSession = new MockHttpSession();

        testExercise = TemporalExercise.builder()
                .name("name")
                .keywords(new ArrayList<>())
                .bodyParts(new ArrayList<>())
                .time(0)
                .build();
        exerciseFirebaseRepository.save(testExercise);
    }

    @AfterAll
    public void cleanUp() {
        userFirebaseRepository.delete(testUser.getId());
    }

    @Test
    public void findCorrectPostCreatesPostInDatabase() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String description = "test description for post";

        ObjectNode postBody = mapper.createObjectNode();
        ArrayNode exerciseIdList = mapper.createArrayNode();
        exerciseIdList.add(testExercise.getId());
        postBody.set("exerciseIdList", exerciseIdList);
        postBody.put("description", description);

        String json = mapper.writeValueAsString(postBody);

        MvcResult result = this.mockMvc.perform(post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .session(authorizedSession))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("id"))
                .andReturn();

        postFirebaseRepository.loadPost((String) result.getModelAndView().getModel().get("id"));
    }

    @Test
    public void testPostCreationWithUnauthorizedUser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String description = "test description for post";

        ObjectNode postBody = mapper.createObjectNode();
        ArrayNode exerciseIdList = mapper.createArrayNode();
        exerciseIdList.add(testExercise.getId());
        postBody.set("exerciseIdList", exerciseIdList);
        postBody.put("description", description);

        String json = mapper.writeValueAsString(postBody);

        this.mockMvc.perform(post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .session(unauthorizedSession))
                .andExpect(status().isUnauthorized());
    }
}
