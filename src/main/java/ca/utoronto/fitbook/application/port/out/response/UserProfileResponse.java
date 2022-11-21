package ca.utoronto.fitbook.application.port.out.response;

import ca.utoronto.fitbook.entity.Exercise;
import ca.utoronto.fitbook.entity.Post;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
public class UserProfileResponse {
    String id;
    String name;
    int followingSize;
    int followerSize;
    String joinDate;
    List<Post> postList;
    List<Post> likedPostList;
    Map<String, Exercise> userExercises;

}
