package ca.utoronto.fitbook.application.port.out.response;

import ca.utoronto.fitbook.entity.User;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
public class UserProfileResponse {
    @NonNull
    User profileUser;
    @NonNull
    List<ProfilePostResponse> postList;
    @NonNull
    List<ProfilePostResponse> likedPostList;
    boolean userFollows;
}
