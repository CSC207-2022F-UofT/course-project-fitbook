package usecase.signup;

import entity.User;

public interface UserRegisterDsGateway {
    boolean findById(String userId);

    void save(UserRegisterDsRequestModel requestModel);
}
