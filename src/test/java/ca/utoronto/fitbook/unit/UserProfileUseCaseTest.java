package ca.utoronto.fitbook.unit;

import ca.utoronto.fitbook.BaseTest;
import ca.utoronto.fitbook.TestUtilities;
import ca.utoronto.fitbook.adapter.persistence.localmemory.ExerciseLocalMemoryRepository;
import ca.utoronto.fitbook.adapter.persistence.localmemory.PostLocalMemoryRepository;
import ca.utoronto.fitbook.adapter.persistence.localmemory.UserLocalMemoryRepository;
import ca.utoronto.fitbook.application.exceptions.EntityNotFoundException;
import ca.utoronto.fitbook.application.port.in.UserProfileUseCase;
import ca.utoronto.fitbook.application.port.in.command.UserProfileCommand;
import ca.utoronto.fitbook.application.port.out.response.PostResponse;
import ca.utoronto.fitbook.application.port.out.response.UserProfileResponse;
import ca.utoronto.fitbook.application.service.UserProfileService;
import ca.utoronto.fitbook.entity.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


public class UserProfileUseCaseTest extends BaseTest {


    private PostLocalMemoryRepository postLocalMemoryRepository;

    private UserLocalMemoryRepository userLocalMemoryRepository;

    private ExerciseLocalMemoryRepository exerciseLocalMemoryRepository;
    private UserProfileUseCase userProfileUseCase;

    private List<Post> randomTestPosts;
    private List<User> randomTestUsers;
    private List<Exercise> randomTestExercises;
    private User firstRandomUser;
    private User secondRandomUser;
    private User lastRandomUser;

    @BeforeAll
    public void init() {
        this.postLocalMemoryRepository = new PostLocalMemoryRepository();
        this.userLocalMemoryRepository = new UserLocalMemoryRepository();
        this.exerciseLocalMemoryRepository = new ExerciseLocalMemoryRepository();
        this.userProfileUseCase = new UserProfileService(userLocalMemoryRepository, userLocalMemoryRepository, postLocalMemoryRepository, exerciseLocalMemoryRepository);

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
            Post randomPost1 = TestUtilities.randomPost(randomUser.getId());
            Post randomPost2 = TestUtilities.randomPost(randomUser.getId());
            Post randomPost3 = TestUtilities.randomPost(randomUser.getId());
            Post randomPost4 = TestUtilities.randomPost(randomUser.getId());
            randomPost1.getExerciseIdList().addAll(List.of(randomRepExercise1.getId(),
                    randomRepExercise2.getId(),
                    randomTempExercise1.getId(),
                    randomTempExercise2.getId()
            ));
            randomPost2.getExerciseIdList().addAll(List.of(randomRepExercise1.getId(),
                    randomRepExercise2.getId(),
                    randomTempExercise1.getId(),
                    randomTempExercise2.getId()
            ));
            randomPost3.getExerciseIdList().addAll(List.of(randomRepExercise1.getId(),
                    randomRepExercise2.getId(),
                    randomTempExercise1.getId(),
                    randomTempExercise2.getId()
            ));
            randomPost4.getExerciseIdList().addAll(List.of(randomRepExercise1.getId(),
                    randomRepExercise2.getId(),
                    randomTempExercise1.getId(),
                    randomTempExercise2.getId()
            ));
            randomUser.getPostIdList().addAll(List.of(randomPost1.getId(),
                    randomPost2.getId(),
                    randomPost3.getId(),
                    randomPost4.getId()));
            randomUser.getLikedPostIdList().addAll(List.of(randomPost1.getId(),
                    randomPost2.getId(),
                    randomPost3.getId(),
                    randomPost4.getId()));

            userLocalMemoryRepository.save(randomUser);
            randomTestUsers.add(randomUser);
            postLocalMemoryRepository.save(randomPost1);
            postLocalMemoryRepository.save(randomPost2);
            postLocalMemoryRepository.save(randomPost3);
            postLocalMemoryRepository.save(randomPost4);
            randomTestPosts.addAll(List.of(randomPost1,
                    randomPost2,
                    randomPost3,
                    randomPost4));
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
        secondRandomUser = randomTestUsers.get(1);
        lastRandomUser = randomTestUsers.get(randomTestUsers.size() - 1);
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
    public void findSelfRandomProfileReturnsUserWithValidAttributes() {
        UserProfileCommand userProfileCommand = new UserProfileCommand(firstRandomUser.getId(), firstRandomUser.getId());

        UserProfileResponse response = this.userProfileUseCase.findProfile(userProfileCommand);

        Assertions.assertEquals(firstRandomUser.getId(), response.getProfileUser().getId());
        Assertions.assertEquals(firstRandomUser.getName(), response.getProfileUser().getName());
        Assertions.assertEquals(firstRandomUser.getTotalLikes(), response.getProfileUser().getTotalLikes());
        Assertions.assertFalse(response.isUserFollows());
    }
    @Test
    public void findSelfRandomProfileReturnsUserPostsWithValidAttributes() {
        UserProfileCommand userProfileCommand = new UserProfileCommand(firstRandomUser.getId(), firstRandomUser.getId());

        UserProfileResponse response = this.userProfileUseCase.findProfile(userProfileCommand);

        Assertions.assertEquals(firstRandomUser.getPostIdList().size(), response.getProfileUser().getPostIdList().size());
        Assertions.assertEquals(firstRandomUser.getLikedPostIdList().size(), response.getProfileUser().getLikedPostIdList().size());
        Assertions.assertFalse(response.isUserFollows());
    }

    @Test
    public void findExistingProfileReturnsUserWithValidAttributes() {
        UserProfileCommand userProfileCommand = new UserProfileCommand(secondRandomUser.getId(), firstRandomUser.getId());

        UserProfileResponse response = this.userProfileUseCase.findProfile(userProfileCommand);

        Assertions.assertEquals(secondRandomUser.getId(), response.getProfileUser().getId());
        Assertions.assertEquals(secondRandomUser.getName(), response.getProfileUser().getName());
        Assertions.assertEquals(secondRandomUser.getTotalLikes(), response.getProfileUser().getTotalLikes());
        Assertions.assertEquals(secondRandomUser.getPostIdList().size(), response.getPostList().size());
        Assertions.assertEquals(secondRandomUser.getFollowersIdList().size(), response.getProfileUser().getFollowersIdList().size());
        Assertions.assertFalse(response.isUserFollows());
    }

    @Test
    public void testProfilePostsInDescendingDate() {
        UserProfileCommand userProfileCommand = new UserProfileCommand(lastRandomUser.getId(), firstRandomUser.getId());

        UserProfileResponse response = this.userProfileUseCase.findProfile(userProfileCommand);

        List<PostResponse> profilePosts = response.getPostList();

        // Make sure every liked post in the profile response is older than the last initial post
        for (int i = 1; i < profilePosts.size(); i ++)
            Assertions.assertTrue(profilePosts.get(i - 1).getPost().getPostDate()
                    .after(profilePosts.get(i).getPost().getPostDate()));
    }

    @Test
    public void testProfileLikedPostsInDescendingDate() {
        UserProfileCommand userProfileCommand = new UserProfileCommand(lastRandomUser.getId(), secondRandomUser.getId());

        UserProfileResponse response = this.userProfileUseCase.findProfile(userProfileCommand);

        List<PostResponse> profileLikedPosts = response.getLikedPostList();

        // Make sure every liked post in the profile response is older than the last initial post
        for (int i = 1; i < profileLikedPosts.size(); i ++)
            Assertions.assertTrue(profileLikedPosts.get(i - 1).getPost().getPostDate()
                    .after(profileLikedPosts.get(i).getPost().getPostDate()));
    }
    @Test
    public void findExistingProfileReturnsPostCorrectInfo() {
        UserProfileCommand userProfileCommand = new UserProfileCommand(firstRandomUser.getId(), lastRandomUser.getId());

        UserProfileResponse response = this.userProfileUseCase.findProfile(userProfileCommand);

        Assertions.assertEquals(firstRandomUser.getPostIdList().size(), response.getPostList().size());
        Assertions.assertEquals(firstRandomUser.getLikedPostIdList().size(), response.getProfileUser().getLikedPostIdList().size());
        Assertions.assertNotNull(response.getPostList().get(0).getPost().getDescription());
        Assertions.assertNotNull(response.getPostList().get(0).getPost().getAuthorId());
        Assertions.assertNotNull(response.getPostList().get(0).getPost().getPostDate());
        Assertions.assertFalse(response.getPostList().get(0).isUserLiked());
        Assertions.assertEquals(2, response.getPostList().get(0).getRepetitiveExerciseList().size());
        Assertions.assertEquals(2, response.getLikedPostList().get(0).getTemporalExerciseList().size());
    }

    @Test
    public void findNoneExistentUserProfileThrowsEntityNotFoundException() {
        String id = "-1";
        UserProfileCommand userProfileCommand = new UserProfileCommand(id, id);
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.userProfileUseCase.findProfile(userProfileCommand));
    }

}
