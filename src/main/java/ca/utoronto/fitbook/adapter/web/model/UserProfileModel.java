package ca.utoronto.fitbook.adapter.web.model;

import ca.utoronto.fitbook.application.port.out.response.ProfilePostResponse;
import ca.utoronto.fitbook.application.port.out.response.UserProfileResponse;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
public class UserProfileModel {
    @NonNull
    String profileId;
    @NonNull
    String name;
    int followingSize;
    int followerSize;
    @NonNull
    String joinDate;
    @NonNull
    List<ProfilePostResponse> postList;
    @NonNull
    List<ProfilePostResponse> likedPostList;
    int totalLikes;
    public static UserProfileModel fromResponseToModel(UserProfileResponse response){
        return new UserProfileModel(
                response.getProfileId(),
                response.getName(),
                response.getFollowingSize(),
                response.getFollowerSize(),
                response.getJoinDate(),
                response.getPostList(),
                response.getLikedPostList(),
                response.getTotalLikes()
        );
    }
}