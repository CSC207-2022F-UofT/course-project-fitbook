package ca.utoronto.fitbook.adapter.web.model;

import ca.utoronto.fitbook.entity.Exercise;
import ca.utoronto.fitbook.entity.Post;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
public class UserProfileModel {
    String id;
    String name;
    int followingSize;
    int followerSize;
    String joinDate;
    List<Post> postList;
    List<Post> likedPostList;
    Map<String, Exercise> userExercises;
}
