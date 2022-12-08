package ca.utoronto.fitbook.application.service;

import ca.utoronto.fitbook.application.port.in.LoadExerciseListPort;
import ca.utoronto.fitbook.application.port.in.LoadUserListPort;
import ca.utoronto.fitbook.application.port.out.response.PostResponse;
import ca.utoronto.fitbook.entity.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public final class PostListToPostResponseMapper {

    /**
     * Converts list of requested user's posts to post responses with full information.
     * Invokes loadExerciseList and loadUser functions to convert id's to full entities
     *
     * @param currentUser current session's user entity
     * @param postList    list of requested user's posts
     * @return list of user's posts with full information.
     */
    public static List<PostResponse> map(User currentUser, List<Post> postList,
                                         LoadExerciseListPort loadExerciseListPort,
                                         LoadUserListPort loadUserListPort) {
        HashSet<String> exerciseIds = new HashSet<>();
        for (Post post : postList) {
            exerciseIds.addAll(post.getExerciseIdList());
        }

        List<Exercise> exerciseList = loadExerciseListPort.loadExerciseList(new ArrayList<>(exerciseIds));
        HashMap<String, Exercise> exerciseIdToExerciseMap = new HashMap<>();
        for (Exercise exercise : exerciseList) {
            exerciseIdToExerciseMap.put(exercise.getId(), exercise);
        }

        // Concatenates all user id's from all posts
        List<String> userIdList = new ArrayList<>();
        for (Post post : postList) {
            userIdList.add(post.getAuthorId());
        }
        // Maps all user ids to their respective User entity
        List<User> userList = loadUserListPort.loadUserList(userIdList);
        HashMap<String, User> userIdToUserMap = new HashMap<>();
        for (User user : userList) {
            userIdToUserMap.put(user.getId(), user);
        }

        List<PostResponse> searchPostResponseList = new ArrayList<>();
        for (Post post : postList) {
            List<TemporalExercise> temporalExerciseList = new ArrayList<>();
            List<RepetitiveExercise> repetitiveExerciseList = new ArrayList<>();

            for (String exerciseId : post.getExerciseIdList()) {
                Exercise exercise = exerciseIdToExerciseMap.get(exerciseId);
                if (exercise instanceof TemporalExercise)
                    temporalExerciseList.add((TemporalExercise) exercise);
                else if (exercise instanceof RepetitiveExercise)
                    repetitiveExerciseList.add((RepetitiveExercise) exercise);
            }

            searchPostResponseList.add(new PostResponse(
                    post,
                    userIdToUserMap.get(post.getAuthorId()).getName(),
                    repetitiveExerciseList,
                    temporalExerciseList,
                    currentUser.getLikedPostIdList().contains(post.getId())
            ));
        }

        return searchPostResponseList;
    }
}
