package ca.utoronto.fitbook.application.service;

import ca.utoronto.fitbook.application.port.in.LoadExerciseListPort;
import ca.utoronto.fitbook.application.port.in.LoadPostListPort;
import ca.utoronto.fitbook.application.port.in.LoadUserPort;
import ca.utoronto.fitbook.application.port.in.UserProfileUseCase;
import ca.utoronto.fitbook.application.port.in.command.UserProfileCommand;
import ca.utoronto.fitbook.application.port.out.response.UserProfileResponse;
import ca.utoronto.fitbook.entity.Exercise;
import ca.utoronto.fitbook.entity.Post;
import ca.utoronto.fitbook.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class UserProfileService implements UserProfileUseCase
{
    private final LoadUserPort userProfilePort;
    private final LoadPostListPort loadPostListPort;
    private final LoadExerciseListPort loadExerciseListPort;

    /**
     * @param command the user's input
     * @return a profile response with user information
     */
    @Override
    public UserProfileResponse createProfile(UserProfileCommand command) {
        User user = userProfilePort.loadUser(command.getUserId());
        List<Post> userPosts = loadPostListPort.loadPostList(user.getPostIdList());
        List<Post> likedPosts = loadPostListPort.loadPostList(user.getLikedPostIdList());
        Map<String, Exercise> userExercises = new HashMap<>();

        for(Post post : userPosts){
            List<Exercise> postExercises = loadExerciseListPort.loadExerciseList(post.getExerciseIdList());
            for(Exercise exercise: postExercises){
                userExercises.put(exercise.getId(), exercise);
            }
        }

        DateFormat dateFormatter = new SimpleDateFormat("MMMM dd, yyyy");
        String cleanDate = dateFormatter.format(user.getJoinDate());


        return new UserProfileResponse(user.getId(), user.getName(), user.getFollowingIdList().size(), user.getFollowersIdList().size(), cleanDate, userPosts, likedPosts, userExercises, user.getTotalLikes());
    }
}
