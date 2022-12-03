package ca.utoronto.fitbook.unit;

import ca.utoronto.fitbook.BaseTest;
import ca.utoronto.fitbook.adapter.persistence.localmemory.ExerciseLocalMemoryRepository;
import ca.utoronto.fitbook.adapter.persistence.localmemory.PostLocalMemoryRepository;
import ca.utoronto.fitbook.adapter.persistence.localmemory.UserLocalMemoryRepository;
import ca.utoronto.fitbook.application.port.in.command.SearchCommand;
import ca.utoronto.fitbook.application.port.out.response.SearchResponse;
import ca.utoronto.fitbook.application.service.SearchService;
import ca.utoronto.fitbook.entity.Exercise;
import ca.utoronto.fitbook.entity.Post;
import ca.utoronto.fitbook.entity.RepetitiveExercise;
import ca.utoronto.fitbook.entity.User;
import org.junit.jupiter.api.AfterEach;
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
    private SearchService searchService;

    private User testUser;

    @BeforeAll
    public void init() {
        this.postLocalMemoryRepository = new PostLocalMemoryRepository();
        this.userLocalMemoryRepository = new UserLocalMemoryRepository();
        this.exerciseLocalMemoryRepository = new ExerciseLocalMemoryRepository();
        this.searchService = new SearchService(exerciseLocalMemoryRepository, postLocalMemoryRepository, exerciseLocalMemoryRepository);

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

    @Test
    public void searchPostsReturnsInCorrectOrder() {
        Exercise exercise1 = RepetitiveExercise.builder()
                .reps(5)
                .sets(2)
                .bodyParts(List.of("glute"))
                .keywords(List.of("glute", "squat"))
                .build();

        Exercise exercise2 = RepetitiveExercise.builder()
                .reps(5)
                .sets(2)
                .bodyParts(List.of("bicep"))
                .keywords(List.of("bicep", "curl"))
                .build();

        Exercise exercise3 = RepetitiveExercise.builder()
                .reps(5)
                .sets(2)
                .bodyParts(List.of("glute"))
                .keywords(List.of("squat", "glute"))
                .build();

        exerciseLocalMemoryRepository.save(exercise1);
        exerciseLocalMemoryRepository.save(exercise2);
        exerciseLocalMemoryRepository.save(exercise3);

        Post post1 = Post.builder()
                .postDate(new Date())
                .authorId(this.testUser.getId())
                .description("Best glute workout, helped me a lot!")
                .exerciseIdList(List.of(exercise1.getId()))
                .likes(2).build();

        Post post2 = Post.builder()
                .postDate(new Date())
                .authorId(this.testUser.getId())
                .description("Average glute workout.")
                .exerciseIdList(List.of(exercise3.getId()))
                .likes(2).build();

        Post post3 = Post.builder()
                .postDate(new Date())
                .authorId(this.testUser.getId())
                .description("Amazing bicep workout!")
                .exerciseIdList(List.of(exercise2.getId()))
                .likes(2)
                .build();

        postLocalMemoryRepository.save(post1);
        postLocalMemoryRepository.save(post2);
        postLocalMemoryRepository.save(post3);

        SearchCommand searchCommand = new SearchCommand("Best glute workout");
        SearchResponse response = this.searchService.search(searchCommand);
        Assertions.assertEquals(2, response.getPostList().size());
        Assertions.assertEquals(post1.getId(), response.getPostList().get(0).getId());

        postLocalMemoryRepository.delete(post1.getId());
        postLocalMemoryRepository.delete(post2.getId());
        postLocalMemoryRepository.delete(post3.getId());
        exerciseLocalMemoryRepository.delete(exercise1.getId());
        exerciseLocalMemoryRepository.delete(exercise2.getId());
        exerciseLocalMemoryRepository.delete(exercise3.getId());
    }

    @Test
    public void searchPostsWithBadQueryReturnsEmptyList() {
        Exercise exercise1 = RepetitiveExercise.builder()
                .reps(5)
                .sets(2)
                .bodyParts(List.of("glute"))
                .keywords(List.of("glute", "squat"))
                .build();

        exerciseLocalMemoryRepository.save(exercise1);

        Post post1 = Post.builder()
                .postDate(new Date())
                .authorId(this.testUser.getId())
                .description("Best glute workout, helped me a lot!")
                .exerciseIdList(List.of(exercise1.getId()))
                .likes(2).build();

        postLocalMemoryRepository.save(post1);

        SearchCommand searchCommand = new SearchCommand("Abrakadabtra");
        SearchResponse response = this.searchService.search(searchCommand);
        Assertions.assertEquals(0, response.getPostList().size());

        postLocalMemoryRepository.delete(post1.getId());
        exerciseLocalMemoryRepository.delete(exercise1.getId());
    }
}
