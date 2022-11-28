package ca.utoronto.fitbook.application.port.out.response;

import ca.utoronto.fitbook.entity.Exercise;
import ca.utoronto.fitbook.entity.User;
import lombok.*;

import java.util.List;

@Value
public class ProfilePostResponse {
    @NonNull
    String postId;
    @NonNull
    User author;
    int likes;
    @NonNull
    String postDate;
    @NonNull
    List<Exercise> exerciseList;
    @NonNull
    String description;
    boolean userLiked;
}
