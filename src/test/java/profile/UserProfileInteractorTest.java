package profile;


import entity.User;
import org.junit.jupiter.api.Test;
import usecase.profile.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


class UserProfileInteractorTest {

    @Test
    void create() {
        UserProfileDsGateway userRepository = new InMemoryUser();

        UserProfilePresenter presenter = new UserProfilePresenter() {
            @Override
            public UserProfileResponseModel prepareSuccessView(UserProfileResponseModel user) {
                // Check that the Output Data and associated changes
                // are correct
                assertEquals("jpablo2002", user.getName());
                assertNotNull(user.getJoinDate()); // any creation time is fine.
                assertEquals(0, user.getFollowerSize());
                assertNotNull(userRepository.findById(user.getId()));
                return null;
            }

            @Override
            public UserProfileResponseModel prepareFailView(String error) {
                fail("Use case failure is unexpected.");
                return null;
            }
        };

        User.UserBuilder userBuilder = new User.UserBuilder();
        Date now = new Date();
        User user1 = userBuilder.withJoinDate(now).withName("jpablo2002").build();
        userRepository.save(user1);
        User user2 = userBuilder.withJoinDate(now).withName("john03").build();
        userRepository.save(user2);
        User user3 = userBuilder.withJoinDate(now).withName("bappoChapo").build();
        userRepository.save(user3);

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, presenter);
        UserProfileRequestModel inputData1 = new UserProfileRequestModel(
                user1.getId());

        interactor.create(inputData1);
    }

    private class InMemoryUser implements UserProfileDsGateway{
        final private Map<String, User> users = new HashMap<>();

        /**
         * @param identifier the user's username
         * @return whether the user exists
         */
        @Override
        public User findById(String identifier) {
            return users.get(identifier);
        }
        @Override
        public void save(User user){
            System.out.println("Save " + user.getName());
            users.put(user.getId(), user);
        }
    }
}
