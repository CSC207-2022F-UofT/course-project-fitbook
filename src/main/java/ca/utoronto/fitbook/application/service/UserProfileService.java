package ca.utoronto.fitbook.application.service;

import ca.utoronto.fitbook.application.port.in.*;
import ca.utoronto.fitbook.application.port.in.command.UserProfileCommand;
import ca.utoronto.fitbook.application.port.out.response.PostResponse;
import ca.utoronto.fitbook.application.port.out.response.UserProfileResponse;
import ca.utoronto.fitbook.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class UserProfileService implements UserProfileUseCase
{
    private final LoadUserPort userProfilePort;
    private final LoadUserListPort loadUserListPort;
    private final LoadPostListPort loadPostListPort;
    private final LoadExerciseListPort loadExerciseListPort;

    /**
     * @param command the user's input
     * @return a profile response containing user information
     */
    @Override
    public UserProfileResponse findProfile(UserProfileCommand command) {
        // Check that the request user and current user exist
        User profileUser = userProfilePort.loadUser(command.getProfileId());
        User currUser = userProfilePort.loadUser(command.getUserId());

        // Check that the requested user's posts and liked posts exist.
        List<Post> posts = loadPostListPort.loadPostList(profileUser.getPostIdList());
        List<Post> likedPosts = loadPostListPort.loadPostList(profileUser.getLikedPostIdList());

        // Sort lists of posts in reverse chronological order
        posts.sort(Comparator.comparing(Post::getPostDate).reversed());
        likedPosts.sort(Comparator.comparing(Post::getPostDate).reversed());

        // Convert list of profile posts to post responses with full information
        List<PostResponse> profilePosts = PostListToPostResponseMapper.map(currUser, posts,
                loadExerciseListPort, loadUserListPort);
        List<PostResponse> profileLikedPosts = PostListToPostResponseMapper.map(currUser, likedPosts,
                loadExerciseListPort, loadUserListPort);

        boolean userFollows = currUser.getFollowingIdList().contains(profileUser.getId());

        // Create response to return all relevant user's profile information
        return new UserProfileResponse(
                profileUser,
                profilePosts,
                profileLikedPosts,
                userFollows);
    }
}