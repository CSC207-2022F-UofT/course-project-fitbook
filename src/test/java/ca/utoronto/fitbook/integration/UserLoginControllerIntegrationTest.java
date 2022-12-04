package ca.utoronto.fitbook.integration;

import ca.utoronto.fitbook.adapter.persistence.firebase.UserFirebaseRepository;
import ca.utoronto.fitbook.application.port.in.UserLoginUseCase;
import ca.utoronto.fitbook.application.port.in.command.UserLoginCommand;
import ca.utoronto.fitbook.application.port.out.response.UserLoginResponse;
import ca.utoronto.fitbook.application.service.UserLoginService;
import ca.utoronto.fitbook.entity.User;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserLoginControllerIntegrationTest extends ControllerBaseIntegrationTest{
    @Autowired
    private UserFirebaseRepository userFirebaseRepository;

    private User testUser;
    @BeforeAll
    public void init() {

        testUser = User.builder()
                .followersIdList(new ArrayList<>())
                .joinDate(new Date())
                .name("jhon")
                .totalLikes(1)
                .password("123123123")
                .postIdList(new ArrayList<>())
                .followingIdList(new ArrayList<>())
                .followersIdList(new ArrayList<>())
                .likedPostIdList(new ArrayList<>())
                .build();

        userFirebaseRepository.save(testUser);

    }



    @AfterAll
    public void cleanUp() {
        userFirebaseRepository.delete(testUser.getId());
    }

    @Test
    public void successfullyLogTheUserInandReturnTheHomePage() throws Exception {
            this.mockMvc.perform(post(String.format("/login"))
                            .param("name","jhon")
                            .param("password","123123123"))
                            .andExpect(status().isOk())
                            .andExpect(content().contentType("text/html;charset=UTF-8"));

        }

    }


