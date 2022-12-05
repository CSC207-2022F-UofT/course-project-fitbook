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


public class UserProfileUseCaseTest extends BaseTest {


    private PostLocalMemoryRepository postLocalMemoryRepository;

    private UserLocalMemoryRepository userLocalMemoryRepository;

    private ExerciseLocalMemoryRepository exerciseLocalMemoryRepository;
    private UserProfileService userProfileService;

    private User testUser1;
    private User testUser2;
    private Exercise exercise1;
    private Post post1;

    @BeforeAll
    public void init() {
        this.postLocalMemoryRepository = new PostLocalMemoryRepository();
        this.userLocalMemoryRepository = new UserLocalMemoryRepository();
        this.exerciseLocalMemoryRepository = new ExerciseLocalMemoryRepository();
        this.userProfileService = new UserProfileService(userLocalMemoryRepository, postLocalMemoryRepository, exerciseLocalMemoryRepository);

        List<String> bodyParts1 = new ArrayList<>();
        bodyParts1.add("rectus abdominis");
        bodyParts1.add("transverse abdominis");
        bodyParts1.add("obliques");

        List<String> keywords1 = new ArrayList<>();
        keywords1.add("abs");
        bodyParts1.add("sit-ups");

        exercise1 = TemporalExercise.builder()
                .name("Sit-ups")
                .bodyParts(bodyParts1)
                .keywords(keywords1)
                .build();

        exerciseLocalMemoryRepository.save(exercise1);

        List<String> exerciseIdList1 = new ArrayList<>();
        exerciseIdList1.add(exercise1.getId());

        post1 = Post.builder()
                .postDate(new Date())
                .exerciseIdList(exerciseIdList1)
                .authorId("1912")
                .description("My favourite morning workout! :)")
                .likes(0)
                .build();

        postLocalMemoryRepository.save(post1);

        List<String> postIdList1 = new ArrayList<>();
        postIdList1.add(post1.getId());

        List<String> followingIdList1 = new ArrayList<>();
        followingIdList1.add("2002");

        testUser1 = User.builder()
                .id("1912")
                .followersIdList(new ArrayList<>())
                .joinDate(new Date())
                .name("Jan")
                .totalLikes(1)
                .password("pw")
                .postIdList(postIdList1)
                .followingIdList(followingIdList1)
                .followersIdList(new ArrayList<>())
                .likedPostIdList(new ArrayList<>())
                .build();
        userLocalMemoryRepository.save(testUser1);

        List<String> followerIdList2 = new ArrayList<>();
        followerIdList2.add("2002");

        testUser2 = User.builder()
                .id("2002")
                .followersIdList(new ArrayList<>())
                .joinDate(new Date())
                .name("Pedro")
                .totalLikes(0)
                .password("us")
                .postIdList(new ArrayList<>())
                .followingIdList(new ArrayList<>())
                .followersIdList(followerIdList2)
                .likedPostIdList(postIdList1)
                .build();
        userLocalMemoryRepository.save(testUser2);
    }

    @AfterAll
    public void cleanUp() {
        userLocalMemoryRepository.delete(testUser1.getId());
        userLocalMemoryRepository.delete(testUser2.getId());
        postLocalMemoryRepository.delete(post1.getId());
        exerciseLocalMemoryRepository.delete(exercise1.getId());
    }

    @Test
    public void findExistingProfileReturnsUserWithValidAttributes() {
        UserProfileCommand userProfileCommand = new UserProfileCommand(testUser.getId(), testUser.getId());

        UserProfileResponse response = this.userProfileService.findProfile(userProfileCommand);

        Assertions.assertEquals(testUser.getId(), response.getProfileId());
        Assertions.assertEquals("Jan", response.getName());
        Assertions.assertEquals(1, response.getTotalLikes());
    }

    @Test
    public void findNoneExistentUserProfileThrowsEntityNotFoundException() {
        String id = "-1";
        UserProfileCommand userProfileCommand = new UserProfileCommand(id, id);
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.userProfileService.findProfile(userProfileCommand));
    }

}
