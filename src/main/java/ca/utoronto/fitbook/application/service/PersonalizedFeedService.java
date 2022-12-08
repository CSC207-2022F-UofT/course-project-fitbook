package ca.utoronto.fitbook.application.service;

import ca.utoronto.fitbook.application.port.in.*;
import ca.utoronto.fitbook.application.port.in.command.PersonalizedFeedCommand;
import ca.utoronto.fitbook.application.port.out.response.PersonalizedFeedResponse;
import ca.utoronto.fitbook.application.port.out.response.PostResponse;
import ca.utoronto.fitbook.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PersonalizedFeedService implements PersonalizedFeedUseCase
{
    private final LoadUserPort loadUserPort;
    private final LoadPaginatedPosts loadPaginatedPostsPort;
    private final LoadExerciseListPort loadExerciseListPort;
    private final LoadUserListPort loadUserListPort;

    /**
     * @param command Personalized feed command
     * @return A list of personalized posts
     */
    @Override
    public PersonalizedFeedResponse getFeed(PersonalizedFeedCommand command) {
        // Grab the user
        User user = loadUserPort.loadUser(command.getUserId());

        // Ignore invalid pagination limits
        final int POST_PER_PAGE_LIMIT = command.getLimit();
        if (POST_PER_PAGE_LIMIT <= 0)
            throw new InvalidFeedLimitException(POST_PER_PAGE_LIMIT);

        // Grab `POST_PER_PAGE_LIMIT` number of posts from the database sorted by post date
        List<Post> feedPosts = loadPaginatedPostsPort.loadPaginatedPosts(command.getPaginationKey(), POST_PER_PAGE_LIMIT);
        List<PostResponse> postResponses = new ArrayList<>();
        String nextPaginationKey = null;
        if (feedPosts.size() > 0) {
            // Store the pagination key for the next round if there are more posts
            if (feedPosts.size() == POST_PER_PAGE_LIMIT)
                nextPaginationKey = feedPosts.get(POST_PER_PAGE_LIMIT - 1).getId();
            List<String> likedPostIds = user.getLikedPostIdList();

            // Sort the feed posts based off whether the user liked it first, then how many total likes each post has
            // This algorithm is similar to Reddit's hot score (without measuring how quickly the likes were made)
            Comparator<Post> inUsersLikedList = (p1, p2) -> Boolean.compare(likedPostIds.contains(p1.getId()), likedPostIds.contains(p2.getId()));
            feedPosts.sort(inUsersLikedList.thenComparing(Post::getLikes).reversed());
            // Converts all Post entities to appropriate PostResponse objects for display
            postResponses = PostListToPostResponseMapper.map(user, feedPosts, loadExerciseListPort, loadUserListPort);
        }

        return new PersonalizedFeedResponse(postResponses, nextPaginationKey);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Feed limit is invalid.")
    public static class InvalidFeedLimitException extends RuntimeException
    {
        public InvalidFeedLimitException(int limit) {
            super(String.format("Feed limit of %d invalid.", limit));
        }
    }
}

