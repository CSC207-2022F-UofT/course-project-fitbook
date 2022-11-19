package usecase.profile;

public interface UserProfilePresenter {
    UserProfileResponseModel prepareSuccessView(UserProfileResponseModel user);

    UserProfileResponseModel prepareFailView(String error);
}
