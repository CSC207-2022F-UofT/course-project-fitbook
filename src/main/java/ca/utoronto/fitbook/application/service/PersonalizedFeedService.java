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
            postResponses = findValuesForPostResponseList(user, feedPosts);
        }

        return new PersonalizedFeedResponse(postResponses, nextPaginationKey);
    }

    private List<PostResponse> findValuesForPostResponseList(User currUser, List<Post> postList) {

        // Initializes map and populates map of ExerciseIds to post
        HashMap<String, String> exerciseIdToPostIdMap = new HashMap<>();
        for (Post post : postList){
            for (String exerciseId : post.getExerciseIdList()){
                exerciseIdToPostIdMap.put(exerciseId, post.getId());
            }
        }
        // Loads Exercises based on their ids
        List<String> exerciseIdList = new ArrayList<>();
        for (Post post : postList){
            exerciseIdList.addAll(post.getExerciseIdList());
        }
        List<Exercise> postExerciseList = loadExerciseListPort.loadExerciseList(exerciseIdList);

        // Retrieves repetitive exercises and converts them to RepetitiveExercise objects
        List<RepetitiveExercise> repetitiveExerciseList = new ArrayList<>();
        for (Exercise exercise : postExerciseList){
            if (Exercise.ExerciseType.REPETITIVE.equals(exercise.getType())){
                repetitiveExerciseList.add((RepetitiveExercise) exercise);
            }
        }
        // Retrieves temporal exercises and converts them to TemporalExercise objects
        List<TemporalExercise> temporalExercises = new ArrayList<>();
        for (Exercise exercise : postExerciseList){
            if (Exercise.ExerciseType.TEMPORAL.equals(exercise.getType())){
                temporalExercises.add((TemporalExercise) exercise);
            }
        }
        // Maps postIds to their related Repetitive exercises
        HashMap<String, List<RepetitiveExercise>> postIdToRepetitiveExerciseMap = new HashMap<>();
        for(RepetitiveExercise exercise : repetitiveExerciseList) {
            String postId = exerciseIdToPostIdMap.get(exercise.getId());
            if(!postIdToRepetitiveExerciseMap.containsKey(postId)) {
                postIdToRepetitiveExerciseMap.put(postId, new ArrayList<>(List.of(exercise)));
            } else {
                postIdToRepetitiveExerciseMap.get(postId).add(exercise);
            }
        }
        // Maps postIds to their related Temporal Exercises
        HashMap<String, List<TemporalExercise>> postIdToTemporalExerciseMap = new HashMap<>();
        for(TemporalExercise exercise : temporalExercises) {
            String postId = exerciseIdToPostIdMap.get(exercise.getId());
            if(!postIdToTemporalExerciseMap.containsKey(postId)) {
                postIdToTemporalExerciseMap.put(postId, new ArrayList<>(List.of(exercise)));
            } else {
                postIdToTemporalExerciseMap.get(postId).add(exercise);
            }
        }
        // Concatenates all user id's from all posts
        List<String> userIdList = new ArrayList<>();
        for (Post post: postList){
            userIdList.add(post.getAuthorId());
        }
        // Maps all user ids to their respective User entity
        List<User> userList = loadUserListPort.loadUserList(userIdList);
        HashMap<String, User> userIdToUserMap = new HashMap<>();
        for (User user : userList){
            userIdToUserMap.put(user.getId(), user);
        }

        List<PostResponse> searchPostResponseList = new ArrayList<>();
        for(Post post : postList) {
            searchPostResponseList.add(new PostResponse(
                    post,
                    userIdToUserMap.get(post.getAuthorId()).getName(),
                    Objects.requireNonNullElseGet(postIdToRepetitiveExerciseMap.get(post.getId()), ArrayList::new),
                    Objects.requireNonNullElseGet(postIdToTemporalExerciseMap.get(post.getId()), ArrayList::new),
                    currUser.getLikedPostIdList().contains(post.getId())
            ));
        }

        return searchPostResponseList;
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Feed limit is invalid.")
    public static class InvalidFeedLimitException extends RuntimeException
    {
        public InvalidFeedLimitException(int limit) {
            super(String.format("Feed limit of %d invalid.", limit));
        }
    }
}
