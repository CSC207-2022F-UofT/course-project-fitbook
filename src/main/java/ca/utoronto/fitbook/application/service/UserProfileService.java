package ca.utoronto.fitbook.application.service;

import ca.utoronto.fitbook.application.port.in.LoadExerciseListPort;
import ca.utoronto.fitbook.application.port.in.LoadPostListPort;
import ca.utoronto.fitbook.application.port.in.LoadUserPort;
import ca.utoronto.fitbook.application.port.in.UserProfileUseCase;
import ca.utoronto.fitbook.application.port.in.command.UserProfileCommand;
import ca.utoronto.fitbook.application.port.out.response.ProfilePostResponse;
import ca.utoronto.fitbook.application.port.out.response.UserProfileResponse;
import ca.utoronto.fitbook.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserProfileService implements UserProfileUseCase
{
    private final LoadUserPort userProfilePort;
    private final LoadPostListPort loadPostListPort;
    private final LoadExerciseListPort loadExerciseListPort;

    /**
     * @param command the user's input
     * @return a profile response containing user information
     */
    @Override
    public UserProfileResponse findProfile(UserProfileCommand command) {
        // TODO: Add custom UserNotFoundException
        // Check that the request user and current user exist
        User profileUser = userProfilePort.loadUser(command.getProfileId());
        User currUser = userProfilePort.loadUser(command.getUserId());

        // TODO: Add custom PostNotFoundException
        // Check that the requested user's posts and liked posts exist.
        List<Post> posts = loadPostListPort.loadPostList(profileUser.getPostIdList());
        List<Post> likedPosts = loadPostListPort.loadPostList(profileUser.getLikedPostIdList());

        DateFormat dateFormatter = new SimpleDateFormat("MMMM dd, yyyy");

        // Convert list of profile posts to post responses with full information
        List<ProfilePostResponse> profilePosts = findProfilePosts(currUser, posts, dateFormatter);
        List<ProfilePostResponse> profileLikedPosts = findProfilePosts(currUser, likedPosts, dateFormatter);

        String dateJoined = dateFormatter.format(profileUser.getJoinDate());
        boolean userFollows = currUser.getFollowingIdList().contains(profileUser.getId());

        // Create response to return all relevant user's profile information
        return new UserProfileResponse(
                profileUser.getId(),
                profileUser.getName(),
                profileUser.getFollowingIdList().size(),
                profileUser.getFollowersIdList().size(),
                dateJoined,
                profilePosts,
                profileLikedPosts,
                profileUser.getTotalLikes(),
                userFollows);
    }

    /**
     * Converts list of requested user's posts to post responses with full information.
     * Invokes loadExerciseList and loadUser functions to convert id's to full entities
     * @param currUser current session's user entity
     * @param posts list of requested user's posts
     * @param dateFormatter date formatting object
     * @return list of user's posts with full information.
     */
    private List<ProfilePostResponse> findProfilePosts(User currUser, List<Post> posts, DateFormat dateFormatter) {
        List<ProfilePostResponse> postResponses = new ArrayList<>();
        for (Post post : posts) {
            String dateCreated = dateFormatter.format(post.getPostDate());

            // TODO: Add custom ExerciseNotFoundException
            // Convert post's list of exercise id's to exercise entities
            List<Exercise> postExercises = loadExerciseListPort.loadExerciseList(post.getExerciseIdList());

            List<RepetitiveExercise> repetitiveExercises = filterRepetitiveExercises(postExercises);
            List<TemporalExercise> temporalExercises = filterTemporalExercises(postExercises);

            // Check if current session's user has liked the current post
            boolean userLiked = currUser.getLikedPostIdList().contains(post.getId());

            User postAuthor = userProfilePort.loadUser(post.getAuthorId());

            // Add post response with full post information
            postResponses.add(new ProfilePostResponse(
                    post.getId(),
                    postAuthor,
                    post.getLikes(),
                    dateCreated,
                    repetitiveExercises,
                    temporalExercises,
                    post.getDescription(),
                    userLiked
            ));
        }
        return postResponses;
    }

    private List<TemporalExercise> filterTemporalExercises(List<Exercise> postExercises) {
        List<TemporalExercise> temporalExercises = new ArrayList<>();
        for (Exercise exercise: postExercises){
            if (exercise.getType().equals(Exercise.ExerciseType.TEMPORAL)){
                temporalExercises.add((TemporalExercise) exercise);
            }
        }
        return temporalExercises;
    }
    private List<RepetitiveExercise> filterRepetitiveExercises(List<Exercise> postExercises) {
        List<RepetitiveExercise> repetitiveExercises = new ArrayList<>();
        for (Exercise exercise: postExercises){
            if (exercise.getType().equals(Exercise.ExerciseType.REPETITIVE)){
                repetitiveExercises.add((RepetitiveExercise) exercise);
            }
        }
        return repetitiveExercises;
    }
}