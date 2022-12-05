package ca.utoronto.fitbook.unit;

import ca.utoronto.fitbook.BaseTest;
import ca.utoronto.fitbook.TestUtilities;
import ca.utoronto.fitbook.adapter.persistence.localmemory.PostLocalMemoryRepository;
import ca.utoronto.fitbook.adapter.persistence.localmemory.UserLocalMemoryRepository;
import ca.utoronto.fitbook.application.port.in.PersonalizedFeedUseCase;
import ca.utoronto.fitbook.application.port.in.command.PersonalizedFeedCommand;
import ca.utoronto.fitbook.application.port.out.response.PersonalizedFeedResponse;
import ca.utoronto.fitbook.application.service.PersonalizedFeedService;
import ca.utoronto.fitbook.entity.Post;
import ca.utoronto.fitbook.entity.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


public class PersonalizedFeedServiceTest extends BaseTest
{

    private PostLocalMemoryRepository postLocalMemoryRepository;
    private UserLocalMemoryRepository userLocalMemoryRepository;
    private PersonalizedFeedUseCase personalizedFeedUseCase;

    private List<Post> randomTestPosts;
    private List<User> randomTestUsers;

    @BeforeAll
    public void init() {
        postLocalMemoryRepository = new PostLocalMemoryRepository();
        userLocalMemoryRepository = new UserLocalMemoryRepository();
        personalizedFeedUseCase = new PersonalizedFeedService(userLocalMemoryRepository, postLocalMemoryRepository);
        randomTestPosts = new ArrayList<>();
        randomTestUsers = new ArrayList<>();

        // Create 100 random users and posts
        for (int i = 0; i < 100; i++) {
            User randomUser = TestUtilities.randomUser();
            Post randomPost = TestUtilities.randomPost(randomUser.getId());
            randomUser.getPostIdList().add(randomPost.getId());

            userLocalMemoryRepository.save(randomUser);
            randomTestUsers.add(randomUser);
            postLocalMemoryRepository.save(randomPost);
            randomTestPosts.add(randomPost);
        }
    }

    @AfterAll
    public void cleanUp() {
        for (User user : randomTestUsers)
            userLocalMemoryRepository.delete(user.getId());
        for (Post post : randomTestPosts)
            postLocalMemoryRepository.delete(post.getId());
    }

    @Test
    public void testCorrectNumberOfPostsInFeed() {
        PersonalizedFeedCommand command = new PersonalizedFeedCommand(randomTestUsers.get(0).getId(), null, 50);
        PersonalizedFeedResponse response = personalizedFeedUseCase.getFeed(command);

        Assertions.assertEquals(50, response.getPostList().size());
    }

    @Test
    public void testCorrectNumberOfPostsInPaginatedFeed() {
        PersonalizedFeedCommand initialCommand = new PersonalizedFeedCommand(randomTestUsers.get(0).getId(), null, 60);
        PersonalizedFeedResponse initialResponse = personalizedFeedUseCase.getFeed(initialCommand);

        PersonalizedFeedCommand paginatedCommand = new PersonalizedFeedCommand(randomTestUsers.get(0).getId(),
                initialResponse.getNextPaginationKey(), 60);
        PersonalizedFeedResponse paginatedResponse = personalizedFeedUseCase.getFeed(paginatedCommand);

        Assertions.assertEquals(40, paginatedResponse.getPostList().size());
        Assertions.assertNull(paginatedResponse.getNextPaginationKey());
    }

    @Test
    public void testNoDuplicatesInPagination() {
        PersonalizedFeedCommand initialCommand = new PersonalizedFeedCommand(randomTestUsers.get(0).getId(), null, 60);
        PersonalizedFeedResponse initialResponse = personalizedFeedUseCase.getFeed(initialCommand);

        PersonalizedFeedCommand paginatedCommand = new PersonalizedFeedCommand(randomTestUsers.get(0).getId(),
                initialResponse.getNextPaginationKey(), 60);
        PersonalizedFeedResponse paginatedResponse = personalizedFeedUseCase.getFeed(paginatedCommand);

        // Make sure there is no duplicated posts between lists
        for (Post post : paginatedResponse.getPostList())
            Assertions.assertFalse(initialResponse.getPostList().contains(post));
    }

    @Test
    public void testInvalidPaginationLimit() {
        PersonalizedFeedCommand command = new PersonalizedFeedCommand(randomTestUsers.get(0).getId(), null, -5);
        Assertions.assertThrows(PersonalizedFeedService.InvalidFeedLimitException.class, () -> personalizedFeedUseCase.getFeed(command));
    }

}
