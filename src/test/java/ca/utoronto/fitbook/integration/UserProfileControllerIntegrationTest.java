package ca.utoronto.fitbook.integration;
import ca.utoronto.fitbook.adapter.persistence.firebase.UserFirebaseRepository;
import ca.utoronto.fitbook.entity.User;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserProfileControllerIntegrationTest extends ControllerBaseIntegrationTest {

    @Autowired
    private UserFirebaseRepository userFirebaseRepository;

    @BeforeAll
    public void init() {

        User user = User.builder()
                .id("0001")
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

        userFirebaseRepository.save(user);
    }

    @AfterAll
    public void cleanUp() {
        userFirebaseRepository.delete("0001");
    }


    @Test
    public void profileExistsUserControllerTest() throws Exception {
        //Tests createProfile in userProfileService with existing user, expected result is a valid response with the users name
        String id  = "0001";
        this.mockMvc.perform(get(String.format("/profile/%s", id)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("profile"));
    }

    @Test
    public void profileNotFoundUserControllerTest() throws Exception {
        //Tests createProfile in userProfileService with a nonexistent user, expected result is a thrown EntityNotFoundException
        this.mockMvc.perform(get("/profile/-1"))
                .andExpect(status().is5xxServerError());
    }
}