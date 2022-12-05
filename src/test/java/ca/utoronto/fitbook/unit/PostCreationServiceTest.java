package ca.utoronto.fitbook.unit;

import ca.utoronto.fitbook.BaseTest;
import ca.utoronto.fitbook.adapter.persistence.localmemory.*;
import ca.utoronto.fitbook.application.exceptions.*;
import ca.utoronto.fitbook.application.port.in.UserNotFoundException;
import ca.utoronto.fitbook.application.port.in.command.PostCreationCommand;
import ca.utoronto.fitbook.application.port.out.response.PostCreationResponse;
import ca.utoronto.fitbook.application.service.PostCreationService;
import ca.utoronto.fitbook.entity.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostCreationServiceTest extends BaseTest{

    private PostLocalMemoryRepository postLocalMemoryRepository;

    private UserLocalMemoryRepository userLocalMemoryRepository;

    private ExerciseLocalMemoryRepository exerciseLocalMemoryRepository;

    private PostCreationService postCreationService;

    private User testUser;

    private Exercise testExercise;

    @BeforeAll
    public void init() {
        this.postLocalMemoryRepository = new PostLocalMemoryRepository();
        this.userLocalMemoryRepository = new UserLocalMemoryRepository();
        this.exerciseLocalMemoryRepository = new ExerciseLocalMemoryRepository();

        this.postCreationService = new PostCreationService(
                postLocalMemoryRepository,
                userLocalMemoryRepository,
                userLocalMemoryRepository,
                userLocalMemoryRepository,
                exerciseLocalMemoryRepository);

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
        userLocalMemoryRepository.saveUser(testUser);

        testExercise = TemporalExercise.builder()
                .name("name")
                .keywords(new ArrayList<>())
                .bodyParts(new ArrayList<>())
                .time(0)
                .build();

        exerciseLocalMemoryRepository.save(testExercise);
    }

    @AfterAll
    public void cleanUp() {
        userLocalMemoryRepository.delete(testUser.getId());
        exerciseLocalMemoryRepository.delete(testExercise.getId());
    }

    @Test
    public void findIncorrectUserIdThrowsUserNotFoundException() {
        //  PostCreationCommand with user that does not exist
        PostCreationCommand noUserPost = new PostCreationCommand(
                "12345",
                new ArrayList<>(List.of(testExercise.getId())),
                "description1");

        Assertions.assertThrows(UserNotFoundException.class,
                () -> this.postCreationService.createPost(noUserPost));
    }

    @Test
    public void findEmptyExerciseListThrowsEmptyExerciseListException() {
        //  PostCreationCommand with empty exercise list
        PostCreationCommand emptyListPost = new PostCreationCommand(
                testUser.getId(),
                new ArrayList<>(),
                "description2");

        Assertions.assertThrows(EmptyExerciseListException.class,
                () -> this.postCreationService.createPost(emptyListPost));
    }

    @Test
    public void findIncorrectExerciseIdThrowsExerciseInListNotFoundException() {
        //  PostCreationCommand with empty invalid exercise id in list
        PostCreationCommand noExercisePost = new PostCreationCommand(
                testUser.getId(),
                new ArrayList<>(List.of("12345", testExercise.getId())),
                "description3");

        Assertions.assertThrows(ExerciseInListNotFoundException.class,
                () -> this.postCreationService.createPost(noExercisePost));
    }

    @Test
    public void findLongDescriptionThrowsDescriptionTooLongException() {
        //  PostCreationCommand with description that is too long
        PostCreationCommand maxDescriptionPost = new PostCreationCommand(
                testUser.getId(),
                new ArrayList<>(List.of(testExercise.getId())),
                "This is a mock description for the usecase " +
                        "to check character length, as this description " +
                        "exceeds the maximum allowed length, 100 characters");

        Assertions.assertThrows(DescriptionTooLongException.class,
                () -> this.postCreationService.createPost(maxDescriptionPost));
    }

    @Test
    public void findCorrectPostCreatesPostInDatabase() {
        // Post with all correct information
        PostCreationCommand correctPost = new PostCreationCommand(
                testUser.getId(),
                new ArrayList<>(List.of(testExercise.getId())),
                "description4");

        PostCreationResponse responseData = this.postCreationService.createPost(correctPost);

        Post savedPost = postLocalMemoryRepository.getById(responseData.getPostId());

        Assertions.assertEquals(savedPost.getDescription(), "description4");
        Assertions.assertEquals(savedPost.getLikes(), 0);
        Assertions.assertEquals(savedPost.getAuthorId(), testUser.getId());
        Assertions.assertEquals(savedPost.getExerciseIdList(), new ArrayList<>(List.of(testExercise.getId())));
    }
}
