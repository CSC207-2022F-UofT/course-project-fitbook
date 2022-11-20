package usecase.profile;

import entity.User;

public class UserProfileInteractor implements UserProfileInputBoundary{
    final UserProfileDsGateway userProfileDsGateway;
    final UserProfilePresenter userPresenter;

    public UserProfileInteractor(UserProfileDsGateway userProfileDsGateway, UserProfilePresenter userProfilePresenter){
        this.userProfileDsGateway = userProfileDsGateway;
        this.userPresenter = userProfilePresenter;
    }

    @Override
    public UserProfileResponseModel create(UserProfileRequestModel requestModel){
        UserProfileDsRequestModel userDsModel = new UserProfileDsRequestModel(requestModel.getId());
        User user = userProfileDsGateway.findById(userDsModel.getId());

        UserProfileResponseModel profileResponseModel = new UserProfileResponseModel(user.getId(), user.getName(), user.getFollowingIdList().size(), user.getFollowersIdList().size(), user.getJoinDate().toString(), user.getPostIdList(), user.getLikedPostIdList());
        return userPresenter.prepareSuccessView(profileResponseModel);
    }
}
