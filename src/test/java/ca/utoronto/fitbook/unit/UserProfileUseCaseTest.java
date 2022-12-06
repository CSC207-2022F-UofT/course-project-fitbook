package ca.utoronto.fitbook.unit;

import ca.utoronto.fitbook.BaseTest;
import ca.utoronto.fitbook.adapter.persistence.localmemory.ExerciseLocalMemoryRepository;
import ca.utoronto.fitbook.adapter.persistence.localmemory.PostLocalMemoryRepository;
import ca.utoronto.fitbook.adapter.persistence.localmemory.UserLocalMemoryRepository;
import ca.utoronto.fitbook.application.exceptions.EntityNotFoundException;
import ca.utoronto.fitbook.application.port.in.command.UserProfileCommand;
import ca.utoronto.fitbook.application.port.out.response.UserProfileResponse;
import ca.utoronto.fitbook.application.service.UserProfileService;
import ca.utoronto.fitbook.entity.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class UserProfileUseCaseTest extends BaseTest {


    private PostLocalMemoryRepository postLocalMemoryRepository;

    private UserLocalMemoryRepository userLocalMemoryRepository;

    private ExerciseLocalMemoryRepository exerciseLocalMemoryRepository;
    private UserProfileService userProfileService;

    private User testUser1;
    private User testUser2;
    private User testUser3;
    private Exercise exercise1;
    private Exercise exercise2;
    private Exercise exercise3;
    private Post post1;
    private Post post2;
    private Post post3;

    @BeforeAll
    public void init() {
        this.postLocalMemoryRepository = new PostLocalMemoryRepository();
        this.userLocalMemoryRepository = new UserLocalMemoryRepository();
        this.exerciseLocalMemoryRepository = new ExerciseLocalMemoryRepository();
        this.userProfileService = new UserProfileService(userLocalMemoryRepository, postLocalMemoryRepository, exerciseLocalMemoryRepository);

        exercise1 = RepetitiveExercise.builder()
                .name("Sit-ups")
                .bodyParts(List.of("rectus abdominis, transverse abdominis, obliques"))
                .keywords(List.of("sit-ups", "abs", "shredded"))
                .sets(3)
                .reps(20)
                .build();

        exercise2 = RepetitiveExercise.builder()
                .reps(5)
                .sets(2)
                .bodyParts(List.of("bicep"))
                .keywords(List.of("bicep", "curl"))
                .name("Bicep Curl")
                .build();

        exercise3 = TemporalExercise.builder()
                .time(60)
                .bodyParts(List.of("rectus abdominis", "obliques", "biceps", "triceps", "buttocks"))
                .keywords(List.of("plank", "rectus abdomins", "abs", "buttocks"))
                .name("Plank")
                .build();

        exerciseLocalMemoryRepository.save(exercise1);
        exerciseLocalMemoryRepository.save(exercise2);
        exerciseLocalMemoryRepository.save(exercise3);

        post1 = Post.builder()
                .postDate(new Date())
                .exerciseIdList(List.of(exercise1.getId(), exercise3.getId()))
                .authorId("2002")
                .description("Amazing abs workout to get shredded! #sixpack :)")
                .likes(2)
                .build();

        post2 = Post.builder()
                .postDate(new Date())
                .exerciseIdList(List.of(exercise2.getId()))
                .authorId("2002")
                .description("Will make you have serious guns! ;)")
                .likes(1)
                .build();

        post3 = Post.builder()
                .postDate(new Date())
                .exerciseIdList(List.of(exercise1.getId(), exercise2.getId(), exercise3.getId()))
                .authorId("2005")
                .description("My favourite all in one evening workout! :)")
                .likes(1)
                .build();

        postLocalMemoryRepository.save(post1);
        postLocalMemoryRepository.save(post2);
        postLocalMemoryRepository.save(post3);

        testUser1 = User.builder()
                .id("1912")
                .joinDate(new Date())
                .name("Jan")
                .totalLikes(0)
                .password("pw")
                .postIdList(new ArrayList<>())
                .followingIdList(List.of("2002", "2005"))
                .followersIdList(List.of("2005"))
                .likedPostIdList(List.of(post1.getId()))
                .build();

        testUser2 = User.builder()
                .id("2002")
                .joinDate(new Date())
                .name("Pedro")
                .totalLikes(3)
                .password("us")
                .postIdList(List.of(post2.getId(), post1.getId()))
                .followingIdList(new ArrayList<>())
                .followersIdList(List.of("1912", "2005"))
                .likedPostIdList(List.of(post3.getId()))
                .build();

        testUser3 = User.builder()
                .id("2005")
                .joinDate(new Date())
                .name("Jim")
                .totalLikes(1)
                .password("pb")
                .postIdList(List.of(post3.getId()))
                .followingIdList(List.of("1912", "2002"))
                .followersIdList(List.of("1912"))
                .likedPostIdList(List.of(post1.getId(), post2.getId()))
                .build();

        userLocalMemoryRepository.save(testUser1);
        userLocalMemoryRepository.save(testUser2);
        userLocalMemoryRepository.save(testUser3);
    }

    @AfterAll
    public void cleanUp() {
        userLocalMemoryRepository.delete(testUser1.getId());
        userLocalMemoryRepository.delete(testUser2.getId());
        userLocalMemoryRepository.delete(testUser3.getId());
        postLocalMemoryRepository.delete(post1.getId());
        postLocalMemoryRepository.delete(post2.getId());
        postLocalMemoryRepository.delete(post3.getId());
        exerciseLocalMemoryRepository.delete(exercise1.getId());
        exerciseLocalMemoryRepository.delete(exercise2.getId());
        exerciseLocalMemoryRepository.delete(exercise3.getId());
    }

    @Test
    public void findSelfProfileReturnsUserWithValidAttributes() {
        UserProfileCommand userProfileCommand = new UserProfileCommand(testUser2.getId(), testUser2.getId());

        UserProfileResponse response = this.userProfileService.findProfile(userProfileCommand);

        Assertions.assertEquals(testUser2.getId(), response.getProfileId());
        Assertions.assertEquals("Pedro", response.getName());
        Assertions.assertEquals(3, response.getTotalLikes());
        Assertions.assertEquals(post2.getId(), response.getPostList().get(0).getPostId());
        Assertions.assertEquals(post2.getLikes(), response.getPostList().get(0).getLikes());
        Assertions.assertEquals(post1.getLikes(), response.getPostList().get(1).getLikes());
        Assertions.assertFalse(response.isUserFollows());
    }

    @Test
    public void findExistingProfileReturnsUserWithValidAttributes() {
        UserProfileCommand userProfileCommand = new UserProfileCommand(testUser1.getId(), testUser2.getId());

        UserProfileResponse response = this.userProfileService.findProfile(userProfileCommand);

        Assertions.assertEquals(testUser1.getId(), response.getProfileId());
        Assertions.assertEquals("Jan", response.getName());
        Assertions.assertEquals(0, response.getTotalLikes());
        Assertions.assertEquals(0, response.getPostList().size());
        Assertions.assertEquals(1, response.getFollowerSize());
        Assertions.assertFalse(response.isUserFollows());
    }

    @Test
    public void findExistingProfileReturnsUserMultiplePostsCorrectOrderAndInfo() {
        UserProfileCommand userProfileCommand = new UserProfileCommand(testUser2.getId(), testUser3.getId());

        UserProfileResponse response = this.userProfileService.findProfile(userProfileCommand);

        Assertions.assertEquals(testUser2.getId(), response.getProfileId());
        Assertions.assertEquals("Pedro", response.getName());
        Assertions.assertEquals(3, response.getTotalLikes());
        Assertions.assertEquals(2, response.getPostList().size());
        Assertions.assertEquals(2, response.getFollowerSize());
        Assertions.assertTrue(response.isUserFollows());
        Assertions.assertEquals(post2.getId(), response.getPostList().get(0).getPostId());
        Assertions.assertEquals(post2.getLikes(), response.getPostList().get(0).getLikes());
        Assertions.assertEquals(post2.getDescription(), response.getPostList().get(0).getDescription());
        Assertions.assertEquals(post1.getId(), response.getPostList().get(1).getPostId());
        Assertions.assertEquals(post1.getLikes(), response.getPostList().get(1).getLikes());
        Assertions.assertEquals(post1.getDescription(), response.getPostList().get(1).getDescription());
    }

    @Test
    public void findExistingProfileReturnsPostCorrectInfo() {
        UserProfileCommand userProfileCommand = new UserProfileCommand(testUser3.getId(), testUser2.getId());

        UserProfileResponse response = this.userProfileService.findProfile(userProfileCommand);

        Assertions.assertEquals(testUser3.getId(), response.getProfileId());
        Assertions.assertEquals("Jim", response.getName());
        Assertions.assertEquals(1, response.getTotalLikes());
        Assertions.assertEquals(1, response.getPostList().size());
        Assertions.assertEquals(1, response.getFollowerSize());
        Assertions.assertFalse(response.isUserFollows());
        Assertions.assertEquals(post3.getId(), response.getPostList().get(0).getPostId());
        Assertions.assertEquals(post3.getLikes(), response.getPostList().get(0).getLikes());
        Assertions.assertEquals(post3.getDescription(), response.getPostList().get(0).getDescription());
        Assertions.assertEquals(post3.getAuthorId(), response.getPostList().get(0).getAuthor().getId());
        Assertions.assertNotNull(response.getPostList().get(0).getPostDate());
        Assertions.assertTrue(response.getPostList().get(0).isUserLiked());
        Assertions.assertEquals(exercise1.getId(), response.getPostList().get(0).getRepetitiveExerciseList().get(0).getId());

    }

    @Test
    public void findNoneExistentUserProfileThrowsEntityNotFoundException() {
        String id = "-1";
        UserProfileCommand userProfileCommand = new UserProfileCommand(id, id);
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.userProfileService.findProfile(userProfileCommand));
    }

}
