package ca.utoronto.fitbook.application.service;

import ca.utoronto.fitbook.application.port.in.LoadExerciseListPort;
import ca.utoronto.fitbook.application.port.in.LoadPostListPort;
import ca.utoronto.fitbook.application.port.in.LoadUserPort;
import ca.utoronto.fitbook.application.port.in.UserProfileUseCase;
import ca.utoronto.fitbook.application.port.in.command.UserProfileCommand;
import ca.utoronto.fitbook.application.port.out.response.ProfilePostResponse;
import ca.utoronto.fitbook.application.port.out.response.UserProfileResponse;
import ca.utoronto.fitbook.entity.Exercise;
import ca.utoronto.fitbook.entity.Post;
import ca.utoronto.fitbook.entity.User;
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
     * @return a profile response with user information
     */
    @Override
    public UserProfileResponse findProfile(UserProfileCommand command) {
        User profileUser = userProfilePort.loadUser(command.getProfileId());
        User currUser = userProfilePort.loadUser(command.getUserId());
        List<Post> posts = loadPostListPort.loadPostList(profileUser.getPostIdList());
        List<Post> likedPosts = loadPostListPort.loadPostList(profileUser.getLikedPostIdList());

        List<ProfilePostResponse> profilePosts = new ArrayList<>();
        List<ProfilePostResponse> profileLikedPosts = new ArrayList<>();

        DateFormat dateFormatter = new SimpleDateFormat("MMMM dd, yyyy");

        findProfilePosts(currUser, posts, profilePosts, dateFormatter);

        findProfilePosts(currUser, likedPosts, profilePosts, dateFormatter);

        String dateJoined = dateFormatter.format(profileUser.getJoinDate());

        return new UserProfileResponse(
                profileUser.getId(),
                profileUser.getName(),
                profileUser.getFollowingIdList().size(),
                profileUser.getFollowersIdList().size(),
                dateJoined,
                profilePosts,
                profileLikedPosts,
                profileUser.getTotalLikes());
    }

    private void findProfilePosts(User currUser, List<Post> posts, List<ProfilePostResponse> profilePosts, DateFormat dateFormatter) {
        for (Post post : posts) {
            List<Exercise> postExercises = loadExerciseListPort.loadExerciseList(post.getExerciseIdList());
            String dateCreated = dateFormatter.format(post.getPostDate());
            boolean userLiked = currUser.getLikedPostIdList().contains(post.getId());

            profilePosts.add(new ProfilePostResponse(
                    post.getId(),
                    userProfilePort.loadUser(post.getAuthorId()),
                    post.getLikes(),
                    dateCreated,
                    postExercises,
                    post.getDescription(),
                    userLiked
            ));
        }
    }
}