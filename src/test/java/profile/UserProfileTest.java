package profile;

import entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import usecase.profile.UserProfile;

import java.util.Date;
import java.util.Objects;


public class UserProfileTest {
    @Test
    public void testUserProfileName() {
        User.UserBuilder userBuilder = new User.UserBuilder();
        userBuilder.withName("Juan Acosta");
        userBuilder.withId("ab2b3o98#uf");
        userBuilder.withPassword("password");
        userBuilder.withJoinDate(new Date());
        User user = userBuilder.build();
        UserProfile userProfile = new UserProfile(user);
        Assertions.assertEquals("Juan Acosta", userProfile.getUserName());
    }
    @Test
    public void testUserProfileRender() {
        User.UserBuilder userBuilder = new User.UserBuilder();
        userBuilder.withName("Juan Acosta");
        userBuilder.withId("ab2b3o98#uf");
        userBuilder.withPassword("password");
        userBuilder.withJoinDate(new Date());
        User user = userBuilder.build();
        UserProfile userProfile = new UserProfile(user);
        Assertions.assertEquals("Name: Juan Acosta \n 0 Followers 0 Following \n Joined: Sun Nov 13 21:18:24 EST 2022", userProfile.render());
    }
}
