package usecase.profile;

public interface UserProfileInputBoundary {
    UserProfileResponseModel create(UserProfileRequestModel requestModel);
}
