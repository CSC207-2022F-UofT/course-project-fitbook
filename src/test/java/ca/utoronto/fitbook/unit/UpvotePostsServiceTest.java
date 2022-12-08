package ca.utoronto.fitbook.unit;

import ca.utoronto.fitbook.BaseTest;
import ca.utoronto.fitbook.TestUtilities;
import ca.utoronto.fitbook.adapter.persistence.localmemory.PostLocalMemoryRepository;
import ca.utoronto.fitbook.adapter.persistence.localmemory.UserLocalMemoryRepository;
import ca.utoronto.fitbook.application.port.in.UpvotePostsUsecase;
import ca.utoronto.fitbook.application.port.in.command.UpvotePostsCommand;
import ca.utoronto.fitbook.application.service.UpvotePostsService;
import ca.utoronto.fitbook.entity.Post;
import ca.utoronto.fitbook.entity.User;
import org.junit.jupiter.api.*;

public class UpvotePostsServiceTest extends BaseTest
{
    private UserLocalMemoryRepository userLocalMemoryRepository;
    private PostLocalMemoryRepository postLocalMemoryRepository;
    private UpvotePostsUsecase upvotePostsUsecase;
    private User testPostLiker;
    private User testPostAuthor;
    private Post testPost;

    @BeforeAll
    public void init() {
        this.userLocalMemoryRepository = new UserLocalMemoryRepository();
        this.postLocalMemoryRepository = new PostLocalMemoryRepository();
        this.upvotePostsUsecase = new UpvotePostsService(
                postLocalMemoryRepository,
                userLocalMemoryRepository,
                postLocalMemoryRepository,
                userLocalMemoryRepository
        );
    }

    @AfterAll
    public void cleanUp() {
        userLocalMemoryRepository.delete(testPostLiker.getId());
        userLocalMemoryRepository.delete(testPostAuthor.getId());
        postLocalMemoryRepository.delete(testPost.getId());
    }

    @BeforeEach
    public void initializeTest() {
        testPostLiker = TestUtilities.randomUser();
        testPostAuthor = TestUtilities.randomUser();

        testPost = TestUtilities.randomPost(testPostAuthor.getId());

        userLocalMemoryRepository.save(testPostLiker);
        userLocalMemoryRepository.save(testPostAuthor);
        postLocalMemoryRepository.save(testPost);
    }

    private void reloadTestEntities() {
        testPostLiker = userLocalMemoryRepository.loadUser(testPostLiker.getId());
        testPostAuthor = userLocalMemoryRepository.loadUser(testPostAuthor.getId());
        testPost = postLocalMemoryRepository.loadPost(testPost.getId());
    }

    @Test
    public void testUpvoteSuccessful() {
        UpvotePostsCommand upvoteCommand = new UpvotePostsCommand(
                testPost.getId(),
                testPostLiker.getId());

        int previousPostLikes = testPost.getLikes();
        int previousAuthorLikes = testPostAuthor.getTotalLikes();
        upvotePostsUsecase.upvotePost(upvoteCommand);
        reloadTestEntities();

        Assertions.assertTrue(testPostLiker.getLikedPostIdList().contains(testPost.getId()));
        Assertions.assertEquals(previousPostLikes + 1, testPost.getLikes());
        Assertions.assertEquals(previousAuthorLikes + 1, testPostAuthor.getTotalLikes());
    }

    @Test
    public void testUpvotePostThrowsUserAlreadyLikedException() {
        UpvotePostsCommand upvoteCommand = new UpvotePostsCommand(
                testPost.getId(),
                testPostLiker.getId());

        upvotePostsUsecase.upvotePost(upvoteCommand);

        Assertions.assertThrows(UpvotePostsService.UserAlreadyLikedException.class,
                () -> this.upvotePostsUsecase.upvotePost(upvoteCommand));
    }

}
