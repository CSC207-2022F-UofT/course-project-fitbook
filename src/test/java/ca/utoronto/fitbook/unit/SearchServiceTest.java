package ca.utoronto.fitbook.unit;

import ca.utoronto.fitbook.BaseTest;
import ca.utoronto.fitbook.adapter.persistence.localmemory.ExerciseLocalMemoryRepository;
import ca.utoronto.fitbook.adapter.persistence.localmemory.PostLocalMemoryRepository;
import ca.utoronto.fitbook.adapter.persistence.localmemory.UserLocalMemoryRepository;
import ca.utoronto.fitbook.application.exceptions.EmptyQueryStringException;
import ca.utoronto.fitbook.application.port.in.SearchPostsUseCase;
import ca.utoronto.fitbook.application.port.in.command.SearchCommand;
import ca.utoronto.fitbook.application.port.out.response.SearchResponse;
import ca.utoronto.fitbook.application.service.SearchService;
import ca.utoronto.fitbook.entity.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchServiceTest extends BaseTest {

    private PostLocalMemoryRepository postLocalMemoryRepository;
    private UserLocalMemoryRepository userLocalMemoryRepository;
    private ExerciseLocalMemoryRepository exerciseLocalMemoryRepository;
    private SearchPostsUseCase searchPostsUseCase;

    private User testUser;
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
        this.searchPostsUseCase = new SearchService(exerciseLocalMemoryRepository,
                                                postLocalMemoryRepository,
                                                exerciseLocalMemoryRepository,
                                                exerciseLocalMemoryRepository,
                                                userLocalMemoryRepository);

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

        exercise1 = RepetitiveExercise.builder()
                .reps(5)
                .sets(2)
                .bodyParts(List.of("glute"))
                .name("squat")
                .keywords(List.of("glute", "squat"))
                .build();

        exercise2 = RepetitiveExercise.builder()
                .reps(5)
                .sets(2)
                .bodyParts(List.of("bicep"))
                .keywords(List.of("bicep", "curl"))
                .name("bicep curl")
                .build();

        exercise3 = RepetitiveExercise.builder()
                .reps(5)
                .sets(2)
                .name("squat")
                .bodyParts(List.of("glute"))
                .keywords(List.of("squat", "glute"))
                .build();

        exerciseLocalMemoryRepository.save(exercise1);
        exerciseLocalMemoryRepository.save(exercise2);
        exerciseLocalMemoryRepository.save(exercise3);

        post1 = Post.builder()
                .postDate(new Date())
                .authorId(this.testUser.getId())
                .description("Best glute workout")
                .exerciseIdList(List.of(exercise1.getId()))
                .likes(2).build();

        post2 = Post.builder()
                .postDate(new Date())
                .authorId(this.testUser.getId())
                .description("Average glute workout.")
                .exerciseIdList(List.of(exercise3.getId()))
                .likes(2).build();

        post3 = Post.builder()
                .postDate(new Date())
                .authorId(this.testUser.getId())
                .description("Amazing bicep workout!")
                .exerciseIdList(List.of(exercise2.getId()))
                .likes(2)
                .build();

        postLocalMemoryRepository.save(post1);
        postLocalMemoryRepository.save(post2);
        postLocalMemoryRepository.save(post3);
        userLocalMemoryRepository.save(testUser);
    }

    @AfterAll
    public void cleanUp() {
        postLocalMemoryRepository.delete(post1.getId());
        postLocalMemoryRepository.delete(post2.getId());
        postLocalMemoryRepository.delete(post3.getId());
        exerciseLocalMemoryRepository.delete(exercise1.getId());
        exerciseLocalMemoryRepository.delete(exercise2.getId());
        exerciseLocalMemoryRepository.delete(exercise3.getId());
        userLocalMemoryRepository.delete(testUser.getId());
    }

    @Test
    public void searchPostsReturnsInCorrectOrderAndSize() {
        SearchCommand searchCommand = new SearchCommand("Best glute workout");
        SearchResponse response = this.searchPostsUseCase.search(searchCommand);
        Assertions.assertEquals(2, response.getPostList().size());
        Assertions.assertEquals(post1.getId(), response.getPostList().get(0).getId());
        Assertions.assertEquals(this.testUser.getName(), response.getPostAuthorNames().get(post1.getAuthorId()));
        Assertions.assertTrue(response.getExerciseListMap().containsKey(exercise1.getId()));
    }

    @Test
    public void searchPostsWithBadQueryReturnsEmptyList() {
        SearchCommand searchCommand = new SearchCommand("Abrakadabtra");
        SearchResponse response = this.searchPostsUseCase.search(searchCommand);
        Assertions.assertEquals(0, response.getPostList().size());
    }

    @Test
    public void searchPostsEmptyQueryThrowsEmptyQueryException() {
        SearchCommand searchCommand = new SearchCommand("");
        Assertions.assertThrows(EmptyQueryStringException.class, () -> this.searchPostsUseCase.search(searchCommand));
    }

    @Test
    public void searchPostCorrectPostAttributesReturned() {
        SearchCommand searchCommand = new SearchCommand("Best bicep workout");
        SearchResponse response = this.searchPostsUseCase.search(searchCommand);

        Assertions.assertEquals(1, response.getPostList().size());
        Assertions.assertEquals(post3.getId(), response.getPostList().get(0).getId());
        Assertions.assertEquals(post3.getAuthorId(), response.getPostList().get(0).getAuthorId());
        Assertions.assertEquals(post3.getPostDate(), response.getPostList().get(0).getPostDate());
        Assertions.assertEquals(post3.getDescription(), response.getPostList().get(0).getDescription());
        Assertions.assertEquals(post3.getExerciseIdList().size(), response.getPostList().get(0).getExerciseIdList().size());
    }

    @Test
    public void searchPostCorrectPostAttributesForMultiplePostsReturned() {
        SearchCommand searchCommand = new SearchCommand("Best squat workout");
        SearchResponse response = this.searchPostsUseCase.search(searchCommand);

        Assertions.assertEquals(2, response.getPostList().size());
        Assertions.assertEquals(post1.getId(), response.getPostList().get(0).getId());
        Assertions.assertEquals(post1.getAuthorId(), response.getPostList().get(0).getAuthorId());
        Assertions.assertEquals(post1.getPostDate(), response.getPostList().get(0).getPostDate());
        Assertions.assertEquals(post1.getDescription(), response.getPostList().get(0).getDescription());
        Assertions.assertEquals(post1.getExerciseIdList().size(), response.getPostList().get(0).getExerciseIdList().size());
        Assertions.assertEquals(post2.getId(), response.getPostList().get(1).getId());
        Assertions.assertEquals(post2.getAuthorId(), response.getPostList().get(1).getAuthorId());
        Assertions.assertEquals(post2.getPostDate(), response.getPostList().get(1).getPostDate());
        Assertions.assertEquals(post2.getDescription(), response.getPostList().get(1).getDescription());
        Assertions.assertEquals(post2.getExerciseIdList().size(), response.getPostList().get(1).getExerciseIdList().size());
    }
}
