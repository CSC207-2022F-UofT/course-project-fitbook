package ca.utoronto.fitbook.unit;

import ca.utoronto.fitbook.BaseTest;
import ca.utoronto.fitbook.adapter.persistence.localmemory.UserLocalMemoryRepository;
import ca.utoronto.fitbook.application.port.in.FollowUseCase;
import ca.utoronto.fitbook.application.port.in.command.FollowCommand;
import ca.utoronto.fitbook.application.port.out.response.FollowResponse;
import ca.utoronto.fitbook.application.service.FollowService;
import ca.utoronto.fitbook.entity.User;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FollowUseCaseTest extends BaseTest {

    private UserLocalMemoryRepository userLocalMemoryRepository;
    private FollowUseCase followUseCase;
    private User testUser1;
    private User testUser2;

    @BeforeAll
    public void init() {
        this.userLocalMemoryRepository = new UserLocalMemoryRepository();
        this.followUseCase = new FollowService(userLocalMemoryRepository, userLocalMemoryRepository);

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
    public void resetFollowers(){
        testUser1.setFollowersIdList(new ArrayList<>());
        testUser1.setFollowingIdList(new ArrayList<>());
        testUser2.setFollowersIdList(new ArrayList<>());
        testUser2.setFollowingIdList(new ArrayList<>());
        userLocalMemoryRepository.save(testUser1);
        userLocalMemoryRepository.save(testUser2);
    }


    @Test
    public void followUserReturnsValidResponse() {
        FollowCommand followCommand = new FollowCommand(testUser1.getId(), testUser2.getId());

        FollowResponse followResponse = this.followUseCase.followUser(followCommand);

        Assertions.assertEquals(testUser2.getId(), followResponse.getFolloweeId());
    }
    @Test
    public void followUserFollowsThemselvesThrowsUserSelfFollowingException() {
        FollowCommand followCommand = new FollowCommand(testUser1.getId(), testUser1.getId());

        Assertions.assertThrows(FollowService.UserSelfFollowingException.class, ()->this.followUseCase.followUser(followCommand));
    }

    @Test
    public void followUserRepeatsFollowThrowsUserAlreadyFollowingException() {
        testUser1.setFollowingIdList(List.of(testUser2.getId()));
        testUser2.setFollowersIdList(List.of(testUser1.getId()));
        userLocalMemoryRepository.save(testUser1);
        userLocalMemoryRepository.save(testUser2);

        FollowCommand followCommand = new FollowCommand(testUser1.getId(), testUser2.getId());
        Assertions.assertThrows(FollowService.UserAlreadyFollowingException.class, ()->this.followUseCase.followUser(followCommand));
    }

}
