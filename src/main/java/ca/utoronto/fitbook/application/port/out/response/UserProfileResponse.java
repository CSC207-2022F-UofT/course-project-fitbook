package ca.utoronto.fitbook.application.port.out.response;

import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
public class UserProfileResponse {
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
}
