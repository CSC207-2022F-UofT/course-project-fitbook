package ca.utoronto.fitbook.unit;

import ca.utoronto.fitbook.BaseTest;
import ca.utoronto.fitbook.adapter.persistence.firebase.UserFirebaseRepository;
import ca.utoronto.fitbook.adapter.persistence.localmemory.PostLocalMemoryRepository;
import ca.utoronto.fitbook.adapter.persistence.localmemory.UserLocalMemoryRepository;
import ca.utoronto.fitbook.application.port.in.UpvotePostsUsecase;
import ca.utoronto.fitbook.application.port.in.command.UpvotePostsCommand;
import ca.utoronto.fitbook.application.service.UpvotePostsService;
import ca.utoronto.fitbook.entity.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.*;

public class UpvotePostsServiceTest extends BaseTest {

    @Autowired
    private UserLocalMemoryRepository userLocalMemoryRepository;
    private PostLocalMemoryRepository postLocalMemoryRepository;
    private UpvotePostsUsecase upvotePostsUsecase;
    private User testUser1;
    private User testUser2;

    @BeforeAll
    public void init() {
        this.userLocalMemoryRepository = new UserLocalMemoryRepository();
        this.postLocalMemoryRepository = new PostLocalMemoryRepository();
        this.upvotePostsUsecase = new UpvotePostsService(postLocalMemoryRepository, userLocalMemoryRepository);

        testUser1 = User.builder()
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
        testUser2 = User.builder()
                .followersIdList(new ArrayList<>())
                .joinDate(new Date())
                .name("Feb")
                .totalLikes(1)
                .password("pw2")
                .postIdList(new ArrayList<>())
                .followingIdList(new ArrayList<>())
                .followersIdList(new ArrayList<>())
                .likedPostIdList(new ArrayList<>())
                .build();
        userLocalMemoryRepository.save(testUser1);
        userLocalMemoryRepository.save(testUser2);
    }

    @AfterAll
    public void cleanUp() {
        userLocalMemoryRepository.delete(testUser1.getId());
        userLocalMemoryRepository.delete(testUser2.getId());
    }
    @AfterEach
    public void resetLikes(){
        testUser1.setLikedPostIdList(new ArrayList<>());
        testUser2.setPostIdList(new ArrayList<>());
        userLocalMemoryRepository.save(testUser1);
        userLocalMemoryRepository.save(testUser2);
    }

    @Test
    //checks if post has already been liked
    public void likePostThrowsUserAlreadyLikedException() {
        testUser1.setLikedPostIdList(List.of(UUID.randomUUID().toString()));
        testUser2.setPostIdList(List.of(UUID.randomUUID().toString()));
        userLocalMemoryRepository.save(testUser1);
        userLocalMemoryRepository.save(testUser2);

        UpvotePostsCommand upvotePostsCommand = new UpvotePostsCommand(
                testUser2.getPostIdList().get(0),
                testUser1.getLikedPostIdList().get(0));
        Assertions.assertThrows(UpvotePostsService.UserAlreadyLikedException.class,
                ()->this.upvotePostsUsecase.upvotePost(upvotePostsCommand));
    }

}
