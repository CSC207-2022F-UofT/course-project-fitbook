package ca.utoronto.fitbook.application.service;

import ca.utoronto.fitbook.application.port.in.LoadPostPort;
import ca.utoronto.fitbook.application.port.in.LoadUserPort;
import ca.utoronto.fitbook.application.port.in.UpvotePostsUsecase;
import ca.utoronto.fitbook.application.port.in.command.UpvotePostsCommand;
import ca.utoronto.fitbook.application.port.out.SavePostPort;
import ca.utoronto.fitbook.application.port.out.SaveUserPort;
import ca.utoronto.fitbook.application.port.out.response.UpvotePostsResponse;
import ca.utoronto.fitbook.entity.Post;
import ca.utoronto.fitbook.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

@Service
@RequiredArgsConstructor
public class UpvotePostsService implements UpvotePostsUsecase{

    private final LoadPostPort loadPostPort;
    private final LoadUserPort loadUserPort;
    private final SavePostPort savePostPort;
    private final SaveUserPort saveUserPort;

    /**
     * @param command the likes coming in from a user liking a post and a post being liked
     * @return total likes
     */
    @Override
    public UpvotePostsResponse upvotePost(UpvotePostsCommand command) {
        // Load the relevant entities
        User userPostLiker = loadUserPort.loadUser(command.getUserId());
        Post likedPost = loadPostPort.loadPost(command.getPostId());
        User postAuthor = loadUserPort.loadUser(likedPost.getAuthorId());

        // Don't allow the same post to be liked twice
        if (userPostLiker.getLikedPostIdList().contains(likedPost.getId()))
            throw new UserAlreadyLikedException();

        // Add the post to the user's liked posts list
        userPostLiker.getLikedPostIdList().add(likedPost.getId());

        // Increment the post's likes
        likedPost.setLikes(likedPost.getLikes() + 1);

        // Increment the post author's total likes
        postAuthor.setTotalLikes(postAuthor.getTotalLikes() + 1);

        // Save the updated entities to the database
        saveUserPort.saveUser(userPostLiker);
        savePostPort.savePost(likedPost);
        saveUserPort.saveUser(postAuthor);

        return new UpvotePostsResponse(likedPost.getLikes());
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST, reason = "User has already liked the post.")
    public static class UserAlreadyLikedException extends RuntimeException {
        public UserAlreadyLikedException() {
            super("User has already liked the post.");
        }
    }

}
