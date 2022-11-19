package usecase.profile;

import entity.User;

public interface UserProfileDsGateway {
    User findById(String id);

    void save(User user);
}
