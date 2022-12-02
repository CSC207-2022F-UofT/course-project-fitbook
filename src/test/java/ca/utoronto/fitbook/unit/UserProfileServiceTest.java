package ca.utoronto.fitbook.unit;

import ca.utoronto.fitbook.BaseTest;
import ca.utoronto.fitbook.adapter.persistence.firebase.UserFirebaseRepository;
import ca.utoronto.fitbook.adapter.persistence.localmemory.ExerciseLocalMemoryRepository;
import ca.utoronto.fitbook.adapter.persistence.localmemory.PostLocalMemoryRepository;
import ca.utoronto.fitbook.adapter.persistence.localmemory.UserLocalMemoryRepository;
import ca.utoronto.fitbook.application.port.in.*;
import ca.utoronto.fitbook.application.port.in.command.UserProfileCommand;
import ca.utoronto.fitbook.application.port.out.response.UserProfileResponse;
import ca.utoronto.fitbook.application.service.UserProfileService;
import ca.utoronto.fitbook.entity.Exercise;
import ca.utoronto.fitbook.entity.User;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Date;


public class UserProfileServiceTest extends BaseTest {


    private PostLocalMemoryRepository postLocalMemoryRepository;

    private UserLocalMemoryRepository userLocalMemoryRepository;

    private ExerciseLocalMemoryRepository exerciseLocalMemoryRepository;
    private UserProfileService userProfileService;

    @BeforeAll
    public void init() {
        this.postLocalMemoryRepository = new PostLocalMemoryRepository();
        this.userLocalMemoryRepository = new UserLocalMemoryRepository();
        this.exerciseLocalMemoryRepository = new ExerciseLocalMemoryRepository();
        this.userProfileService = new UserProfileService(userLocalMemoryRepository, postLocalMemoryRepository, exerciseLocalMemoryRepository);

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
        userLocalMemoryRepository.save(user);
    }

    @AfterAll
    public void cleanUp() {
        userLocalMemoryRepository.delete("0001");
    }

    @Test
    public void createProfileTest() {
        //Tests createProfile in userProfileService with existing user, expected result is a valid response with the users name
        String id = "0001";
        UserProfileCommand userProfileCommand = new UserProfileCommand(id);

        UserProfileResponse response = this.userProfileService.createProfile(userProfileCommand);

        Assertions.assertEquals(id, response.getId());
        Assertions.assertEquals("Jan", response.getName());
        Assertions.assertEquals(1, response.getTotalLikes());
    }

    @Test
    public void createProfileUserNotExists() {
        //Tests createProfile in userProfileService with a nonexistent user, expected result is a thrown EntityNotFoundException
        String id = "0002";
        UserProfileCommand userProfileCommand = new UserProfileCommand(id);
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.userProfileService.createProfile(userProfileCommand));
    }

}
