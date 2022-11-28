package ca.utoronto.fitbook.application.service;

import ca.utoronto.fitbook.application.port.in.LoadPostPort;
import ca.utoronto.fitbook.application.port.in.LoadUserPort;
import ca.utoronto.fitbook.application.port.in.UpvotePostsUsecase;
import ca.utoronto.fitbook.application.port.in.command.UpvotePostsCommand;
import ca.utoronto.fitbook.application.port.out.SavePostPort;
import ca.utoronto.fitbook.application.port.out.SaveUserPort;
import ca.utoronto.fitbook.application.port.out.response.UpvotePostsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ca.utoronto.fitbook.entity.Post;
import ca.utoronto.fitbook.entity.User;

@Service
@RequiredArgsConstructor
public class upvotePostsService implements UpvotePostsUsecase{

    private final LoadPostPort loadPostPort;
    private final LoadUserPort loadUserPort;
    private final SavePostPort savePostPort;
    private final SaveUserPort saveUserPort;

    /**
     * @param command the likes coming in from a user liking a post and a post being liked
     * @return null
     */
    @Override
    public UpvotePostsResponse upvotePost(UpvotePostsCommand command) {
        User userPostLiker = loadUserPort.loadUser(command.getUserId());
        Post likedPost = loadPostPort.loadPost(command.getPostId());
        User postAuthor = loadUserPort.loadUser(likedPost.getAuthorId());

        userPostLiker.getLikedPostIdList().add(likedPost.getId());

        return new UpvotePostsResponse(
                userPostLiker.getId(),
                likedPost.getAuthorId(),
                likedPost.getLikes());
    }
}
