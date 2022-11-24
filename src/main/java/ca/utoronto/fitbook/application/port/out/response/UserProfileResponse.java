package ca.utoronto.fitbook.application.port.out.response;

import ca.utoronto.fitbook.entity.Exercise;
import ca.utoronto.fitbook.entity.Post;
import lombok.NonNull;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
public class UserProfileResponse {
    @NonNull
    String id;
    @NonNull
    String name;
    int followingSize;
    int followerSize;
    @NonNull
    String joinDate;
    @NonNull
    List<Post> postList;
    @NonNull
    List<Post> likedPostList;
    @NonNull
    Map<String, Exercise> userExercises;
    int totalLikes;
}
