package ca.utoronto.fitbook.unit;

import ca.utoronto.fitbook.BaseTest;
import ca.utoronto.fitbook.adapter.persistence.localmemory.ExerciseLocalMemoryRepository;
import ca.utoronto.fitbook.adapter.persistence.localmemory.PostLocalMemoryRepository;
import ca.utoronto.fitbook.adapter.persistence.localmemory.UserLocalMemoryRepository;
import ca.utoronto.fitbook.application.exceptions.EntityNotFoundException;
import ca.utoronto.fitbook.application.port.in.command.UserProfileCommand;
import ca.utoronto.fitbook.application.port.out.response.UserProfileResponse;
import ca.utoronto.fitbook.application.service.UserProfileService;
import ca.utoronto.fitbook.entity.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;


public class UserProfileServiceTest extends BaseTest {


    private PostLocalMemoryRepository postLocalMemoryRepository;

    private UserLocalMemoryRepository userLocalMemoryRepository;

    private ExerciseLocalMemoryRepository exerciseLocalMemoryRepository;
    private UserProfileService userProfileService;

    private User testUser;

    @BeforeAll
    public void init() {
        this.postLocalMemoryRepository = new PostLocalMemoryRepository();
        this.userLocalMemoryRepository = new UserLocalMemoryRepository();
        this.exerciseLocalMemoryRepository = new ExerciseLocalMemoryRepository();
        this.userProfileService = new UserProfileService(userLocalMemoryRepository, postLocalMemoryRepository, exerciseLocalMemoryRepository);

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
        userLocalMemoryRepository.save(testUser);
    }

    @AfterAll
    public void cleanUp() {
        userLocalMemoryRepository.delete("0001");
    }

    @Test
    public void findExistingProfileReturnsUserWithValidAttributes() {
        UserProfileCommand userProfileCommand = new UserProfileCommand(testUser.getId());

        UserProfileResponse response = this.userProfileService.createProfile(userProfileCommand);

        Assertions.assertEquals(testUser.getId(), response.getId());
        Assertions.assertEquals("Jan", response.getName());
        Assertions.assertEquals(1, response.getTotalLikes());
    }

    @Test
    public void findNoneExistentUserProfileThrowsEntityNotFoundException() {
        String id = "-1";
        UserProfileCommand userProfileCommand = new UserProfileCommand(id);
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.userProfileService.createProfile(userProfileCommand));
    }

}
