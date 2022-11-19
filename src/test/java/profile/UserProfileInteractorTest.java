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
        // To test the use case:
        // 1) Create a UserRegisterInteractor and prerequisite objects
        //    (arguments for the UserRegisterInteractor constructor parameters)
        // 2) create the Input Data
        // 3) Call the use case User Input Boundary method to run the use case
        // 4) Check that the Output Data passed to the Presenter is correct
        // 5) Check that the expected changes to the data layer are there.

        // 1) UserRegisterInteractor and prerequisite objects
        // We're going to need a place to save and look up information. We could
        // use FileUser, but because unit tests are supposed to be independent
        // that would make us also reset the file when we are done.
        // Instead, we're going to "mock" that info using a short-lived solution
        // that just keeps the info in a dictionary, and it won't be persistent.
        // (Separately, elsewhere, we will need to test the FileUser works
        // properly.)
        UserProfileDsGateway userRepository = new InMemoryUser();

        // This creates an anonymous implementing class for the Output Boundary.
        UserProfilePresenter presenter = new UserProfilePresenter() {
            @Override
            public UserProfileResponseModel prepareSuccessView(UserProfileResponseModel user) {
                // 4) Check that the Output Data and associated changes
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

        // 2) Input data — we can make this up for the test. Normally it would
        // be created by the Controller.
        UserProfileRequestModel inputData = new UserProfileRequestModel(
                "1912");

        // 3) Run the use case
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
