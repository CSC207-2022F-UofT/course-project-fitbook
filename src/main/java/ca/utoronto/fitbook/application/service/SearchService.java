package ca.utoronto.fitbook.application.service;

import ca.utoronto.fitbook.application.exceptions.EmptyQueryStringException;
import ca.utoronto.fitbook.application.port.in.*;
import ca.utoronto.fitbook.application.port.in.command.SearchCommand;
import ca.utoronto.fitbook.application.port.out.response.SearchResponse;
import ca.utoronto.fitbook.entity.Exercise;
import ca.utoronto.fitbook.entity.Post;
import com.google.firebase.database.utilities.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService implements SearchPostsUseCase {

    private final LoadExerciseListByKeywordsPort loadExerciseListByKeywordsPort;
    private final LoadPostListByExerciseListPort loadPostListByExerciseList;
    private final LoadExerciseByBodyPartsPort loadExerciseByBodyPartsPort;
    private final LoadExerciseListPort loadExerciseListPort;
    private final LoadUserPort loadUserPort;

    /**
     * @param searchCommand contains query string
     * @return SearchResponse object that contains posts relevant to query
     */
    @Override
    public SearchResponse search(SearchCommand searchCommand) {
        String queryString = searchCommand.getQueryString();

        //Checks if query string is not empty
        if(queryString.isEmpty())
            throw new EmptyQueryStringException();

        //Breaks up query string into individual words
        List<String> keywordsAndBodyParts = extractKeywords(queryString);

        //Queries exercise collection by keywords and body parts
        List<Exercise> queryExerciseList = new ArrayList<>(loadExerciseByBodyPartsPort.loadExerciseByBodyParts(keywordsAndBodyParts));
        queryExerciseList.addAll(loadExerciseListByKeywordsPort.loadExerciseListByKeywords(keywordsAndBodyParts));

        //Loads posts based on exercises
        List<Post> postList;
        try {
            postList = loadPostListByExerciseList.loadPostListByExerciseList(queryExerciseList.stream().map(Exercise::getId).collect(Collectors.toList()));
        } catch (InterruptedException | ExecutionException e) {
            postList = new ArrayList<>();
        }

        //Get list of all exercises and map them to their ids
        List<Exercise> exerciseList = loadExerciseListPort.loadExerciseList(postList.stream().flatMap(post -> post.getExerciseIdList().stream()).collect(Collectors.toList()));
        HashMap<String, Exercise> exerciseListMap = new HashMap<>();
        exerciseList.forEach(exercise -> exerciseListMap.put(exercise.getId(), exercise));

        //Get List of author names
        HashMap<String, String> postAuthorMap = new HashMap<>();
        postList.forEach(post -> postAuthorMap.put(post.getAuthorId(), loadUserPort.loadUser(post.getAuthorId()).getName()));


        //Calculates similarity between query string and post descriptions
        List<Pair<Post, Double>> weightedPostList = postList.stream().map(post -> new Pair<>(post, findSimilarity(queryString, post.getDescription()))).collect(Collectors.toList());

        //Sorts query results by calculated weight
        Comparator<Pair<Post, Double>> compareByWeight = (Pair<Post, Double> p1, Pair<Post, Double> p2) -> (int) -Math.ceil((p1.getSecond() - p2.getSecond()) * 1000);
        weightedPostList.sort(compareByWeight);

        List<Pair<Post, Double>> shortenedWeightedPostList = new ArrayList<>();

        //Limit search results to 10 posts
        int i = 0;
        while(i < weightedPostList.size() && i < 10)
            shortenedWeightedPostList.add(weightedPostList.get(i++));

        return new SearchResponse(shortenedWeightedPostList.stream().map(Pair::getFirst).collect(Collectors.toList()), exerciseListMap, postAuthorMap);
    }

    /**
     * @param queryString string to be split
     * @return list of strings
     */
    private List<String> extractKeywords(String queryString) {
        String[] words = queryString.split(" ");
        return Arrays.asList(words);
    }

    /**
     * @param queryString query string
     * @param description string
     * @return similarity between the two strings
     */
    private double findSimilarity(String queryString, String description) {
        int vectorDimension = Math.max(queryString.length(), description.length());
        int[] queryVector = new int[vectorDimension];
        int[] descriptionVector = new int[vectorDimension];

        //Vectorizes strings
        for(int i = 0; i < vectorDimension; i++) {
            if(queryString.length() > i)
                queryVector[i] = queryString.charAt(i);

            if(description.length() > i)
                descriptionVector[i] = description.charAt(i);
        }

        //Calculates magnitudes of vectors
        double queryVectorMag = Math.sqrt(Arrays.stream(queryVector).map(entry -> entry * entry).sum());
        double descriptionVectorMag = Math.sqrt(Arrays.stream(descriptionVector).map(entry -> entry * entry).sum());
        //Calculates dotProduct
        double dotProduct = calculateDotProduct(queryVector, descriptionVector);
        //Calculates similarity
        return dotProduct/(descriptionVectorMag * queryVectorMag);
    }

    /**
     * @param vector1 vector
     * @param vector2 vector
     * @return dot product of the two vectors
     */
    private double calculateDotProduct(int[] vector1, int[] vector2) {
        int sum = 0;
        for(int i = 0; i < vector1.length; i++) {
            sum += vector1[i] * vector2[i];
        }
        return sum;
    }
}
