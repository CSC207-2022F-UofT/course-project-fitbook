package ca.utoronto.fitbook.adapter.web.model;

import ca.utoronto.fitbook.entity.Exercise;
import ca.utoronto.fitbook.entity.Post;
import lombok.NonNull;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
public class UserProfileModel {
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
