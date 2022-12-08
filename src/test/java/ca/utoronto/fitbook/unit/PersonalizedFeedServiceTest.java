package ca.utoronto.fitbook.unit;

import ca.utoronto.fitbook.BaseTest;
import ca.utoronto.fitbook.TestUtilities;
import ca.utoronto.fitbook.adapter.persistence.localmemory.ExerciseLocalMemoryRepository;
import ca.utoronto.fitbook.adapter.persistence.localmemory.PostLocalMemoryRepository;
import ca.utoronto.fitbook.adapter.persistence.localmemory.UserLocalMemoryRepository;
import ca.utoronto.fitbook.application.port.in.PersonalizedFeedUseCase;
import ca.utoronto.fitbook.application.port.in.command.PersonalizedFeedCommand;
import ca.utoronto.fitbook.application.port.out.response.PersonalizedFeedResponse;
import ca.utoronto.fitbook.application.port.out.response.PostResponse;
import ca.utoronto.fitbook.application.service.PersonalizedFeedService;
import ca.utoronto.fitbook.entity.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class PersonalizedFeedServiceTest extends BaseTest
{

    private PostLocalMemoryRepository postLocalMemoryRepository;
    private UserLocalMemoryRepository userLocalMemoryRepository;
    private ExerciseLocalMemoryRepository exerciseLocalMemoryRepository;
    private PersonalizedFeedUseCase personalizedFeedUseCase;

    private List<Post> randomTestPosts;
    private List<User> randomTestUsers;
    private List<Exercise> randomTestExercises;
    private User firstRandomUser;

    @BeforeAll
    public void init() {
        postLocalMemoryRepository = new PostLocalMemoryRepository();
        userLocalMemoryRepository = new UserLocalMemoryRepository();
        exerciseLocalMemoryRepository = new ExerciseLocalMemoryRepository();
        personalizedFeedUseCase = new PersonalizedFeedService(userLocalMemoryRepository,
                postLocalMemoryRepository,
                exerciseLocalMemoryRepository,
                userLocalMemoryRepository);
        randomTestPosts = new ArrayList<>();
        randomTestUsers = new ArrayList<>();
        randomTestExercises = new ArrayList<>();

        // Create 100 random users and posts
        for (int i = 0; i < 100; i++) {
            User randomUser = TestUtilities.randomUser();
            RepetitiveExercise randomRepExercise1 = TestUtilities.randomRepetitiveExercise();
            RepetitiveExercise randomRepExercise2 = TestUtilities.randomRepetitiveExercise();
            TemporalExercise randomTempExercise1 = TestUtilities.randomTemporalExercise();
            TemporalExercise randomTempExercise2 = TestUtilities.randomTemporalExercise();
            Post randomPost = TestUtilities.randomPost(randomUser.getId());
            randomPost.getExerciseIdList().addAll(List.of(randomRepExercise1.getId(),
                    randomRepExercise2.getId(),
                    randomTempExercise1.getId(),
                    randomTempExercise2.getId()
                    ));
            randomUser.getPostIdList().add(randomPost.getId());

            userLocalMemoryRepository.save(randomUser);
            randomTestUsers.add(randomUser);
            postLocalMemoryRepository.save(randomPost);
            randomTestPosts.add(randomPost);
            exerciseLocalMemoryRepository.save(randomRepExercise1);
            exerciseLocalMemoryRepository.save(randomRepExercise2);
            exerciseLocalMemoryRepository.save(randomTempExercise1);
            exerciseLocalMemoryRepository.save(randomTempExercise2);
            randomTestExercises.addAll(List.of(randomRepExercise1,
                    randomRepExercise2,
                    randomTempExercise1,
                    randomTempExercise2
            ));
        }

        firstRandomUser = randomTestUsers.get(0);

        randomTestPosts.sort(Comparator.comparing(Post::getPostDate).reversed());
        firstRandomUser.getLikedPostIdList().add(randomTestPosts.get(5).getId());
    }

    @AfterAll
    public void cleanUp() {
        for (User user : randomTestUsers)
            userLocalMemoryRepository.delete(user.getId());
        for (Post post : randomTestPosts)
            postLocalMemoryRepository.delete(post.getId());
        for (Exercise exercise : randomTestExercises)
            exerciseLocalMemoryRepository.delete(exercise.getId());
    }

    @Test
    public void testCorrectNumberOfPostsInFeed() {
        PersonalizedFeedCommand command = new PersonalizedFeedCommand(firstRandomUser.getId(), null, 50);
        PersonalizedFeedResponse response = personalizedFeedUseCase.getFeed(command);

        Assertions.assertEquals(50, response.getPostList().size());
    }

    @Test
    public void testCorrectNumberOfExercisesInPostsInFeed() {
        PersonalizedFeedCommand command = new PersonalizedFeedCommand(firstRandomUser.getId(), null, 50);
        PersonalizedFeedResponse response = personalizedFeedUseCase.getFeed(command);

        Assertions.assertEquals(50, response.getPostList().size());

        for (PostResponse postResponse : response.getPostList()){
            Assertions.assertEquals(2, postResponse.getRepetitiveExerciseList().size());
            Assertions.assertEquals(2, postResponse.getTemporalExerciseList().size());
        }
    }

    @Test
    public void testCorrectNumberOfPostsInPaginatedFeed() {
        PersonalizedFeedCommand initialCommand = new PersonalizedFeedCommand(firstRandomUser.getId(), null, 60);
        PersonalizedFeedResponse initialResponse = personalizedFeedUseCase.getFeed(initialCommand);

        PersonalizedFeedCommand paginatedCommand = new PersonalizedFeedCommand(firstRandomUser.getId(),
                initialResponse.getNextPaginationKey(), 60);
        PersonalizedFeedResponse paginatedResponse = personalizedFeedUseCase.getFeed(paginatedCommand);

        Assertions.assertEquals(40, paginatedResponse.getPostList().size());
        Assertions.assertNull(paginatedResponse.getNextPaginationKey());
    }

    @Test
    public void testLikedFirstInFeed() {
        PersonalizedFeedCommand initialCommand = new PersonalizedFeedCommand(firstRandomUser.getId(), null, 60);
        PersonalizedFeedResponse initialResponse = personalizedFeedUseCase.getFeed(initialCommand);

        PersonalizedFeedCommand paginatedCommand = new PersonalizedFeedCommand(firstRandomUser.getId(),
                initialResponse.getNextPaginationKey(), 60);
        PersonalizedFeedResponse paginatedResponse = personalizedFeedUseCase.getFeed(paginatedCommand);

        Assertions.assertEquals(40, paginatedResponse.getPostList().size());
        Assertions.assertNull(paginatedResponse.getNextPaginationKey());
    }

    @Test
    public void testNoDuplicatesInPagination() {
        PersonalizedFeedCommand initialCommand = new PersonalizedFeedCommand(firstRandomUser.getId(), null, 60);
        PersonalizedFeedResponse initialResponse = personalizedFeedUseCase.getFeed(initialCommand);

        PersonalizedFeedCommand paginatedCommand = new PersonalizedFeedCommand(firstRandomUser.getId(),
                initialResponse.getNextPaginationKey(), 60);
        PersonalizedFeedResponse paginatedResponse = personalizedFeedUseCase.getFeed(paginatedCommand);

        // Make sure there is no duplicated posts between lists
        for (PostResponse post : paginatedResponse.getPostList())
            Assertions.assertFalse(initialResponse.getPostList().contains(post));
    }

    @Test
    public void testPaginationInDescendingDate() {
        PersonalizedFeedCommand initialCommand = new PersonalizedFeedCommand(firstRandomUser.getId(), null, 60);
        PersonalizedFeedResponse initialResponse = personalizedFeedUseCase.getFeed(initialCommand);

        PersonalizedFeedCommand paginatedCommand = new PersonalizedFeedCommand(firstRandomUser.getId(),
                initialResponse.getNextPaginationKey(), 60);
        PersonalizedFeedResponse paginatedResponse = personalizedFeedUseCase.getFeed(paginatedCommand);

        List<PostResponse> initialPosts = initialResponse.getPostList();
        PostResponse lastInitialPost = initialPosts.get(initialPosts.size() - 1);

        // Make sure every post in the paginated response is older than the last initial post
        for (PostResponse post : paginatedResponse.getPostList())
            Assertions.assertTrue(post.getPost().getPostDate().before(lastInitialPost.getPost().getPostDate()));
    }

    @Test
    public void testCorrectNumberOfPostsForInvalidPaginationKey() {
        PersonalizedFeedCommand command = new PersonalizedFeedCommand(firstRandomUser.getId(), "invalid", 50);
        PersonalizedFeedResponse response = personalizedFeedUseCase.getFeed(command);

        Assertions.assertEquals(0, response.getPostList().size());
    }

    @Test
    public void testInvalidPaginationLimit() {
        PersonalizedFeedCommand command = new PersonalizedFeedCommand(firstRandomUser.getId(), null, -5);
        Assertions.assertThrows(PersonalizedFeedService.InvalidFeedLimitException.class, () -> personalizedFeedUseCase.getFeed(command));
    }

}
