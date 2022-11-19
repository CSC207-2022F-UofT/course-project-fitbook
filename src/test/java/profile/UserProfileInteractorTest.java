package profile;


import entity.User;
import org.junit.jupiter.api.Test;
import usecase.profile.*;

import java.util.Date;
import java.util.HashMap;
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
                assertNotNull(userRepository.findById("1912"));
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
        User user = userBuilder.withJoinDate(now).withId("1912").withName("jpablo2002").build();
        userRepository.save(user);
        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, presenter);

        UserProfileRequestModel inputData = new UserProfileRequestModel(
                "1912");

        interactor.create(inputData);
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
