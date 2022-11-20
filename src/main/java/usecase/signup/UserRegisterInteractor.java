package usecase.signup;

import entity.User;

public class UserRegisterInteractor implements UserRegisterInputBoundary{
    final UserRegisterDsGateway userDsGateway;
    final UserRegisterPresenter userPresenter;
    public UserRegisterInteractor(UserRegisterDsGateway userRegisterDfGateway, UserRegisterPresenter userRegisterPresenter) {
        this.userDsGateway = userRegisterDfGateway;
        this.userPresenter = userRegisterPresenter;
    }

    @Override
    public UserRegisterResponseModel create(UserRegisterRequestModel requestModel) {
        if (userDsGateway.findById(requestModel.getId())) {
            return userPresenter.prepareFailView("User already exists.");
        } else if (!requestModel.getPassword().equals(requestModel.getRepeatPassword())) {
            return userPresenter.prepareFailView("Passwords don't match.");
        }else if (requestModel.getName().length()>40){
            return userPresenter.prepareFailView("Name is too long by "+ ((requestModel.getName().length())-40)+" characters");
        }else if (requestModel.getPassword().length()>40){
            return userPresenter.prepareFailView("Password is too short");
        }else if (requestModel.getPassword().length()>40){
            return userPresenter.prepareFailView("Password is too long by "+ ((requestModel.getName().length())-40)+" characters");
        }
        User.UserBuilder userBuilder = new User.UserBuilder();
        userBuilder.withFollowersList(requestModel.getFollowerIdList());
        userBuilder.withFollowingList(requestModel.getFollowingIdList());
        userBuilder.withJoinDate(requestModel.getJoinDate());
        userBuilder.withName(requestModel.getName());
        userBuilder.withPassword(requestModel.getPassword());
        userBuilder.withLikedPostList(requestModel.getLikedPostIdList());
        userBuilder.withPostList(requestModel.getPostIdList());
        userBuilder.withTotalLikes(requestModel.getTotalLikes());

        User user = userBuilder.build();

        UserRegisterDsRequestModel userDsModel = new UserRegisterDsRequestModel(user.getName(),user.getPassword(),user.getId(),user.getJoinDate(),user.getTotalLikes(),user.getFollowingIdList(),user.getFollowersIdList(),user.getPostIdList(),user.getLikedPostIdList());
        userDsGateway.save(userDsModel);

        UserRegisterResponseModel accountResponseModel = new UserRegisterResponseModel(user.getId(), user.getName(), user.getJoinDate(), user.getTotalLikes(), user.getFollowingIdList(), user.getFollowersIdList(), user.getPostIdList(), user.getLikedPostIdList());
        return userPresenter.prepareSuccessView(accountResponseModel);
    }

}
