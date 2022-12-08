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
    List<PostResponse> postList;
    @NonNull
    List<PostResponse> likedPostList;
    boolean userFollows;
}
