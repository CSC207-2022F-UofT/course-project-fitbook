package ca.utoronto.fitbook.application.service;

import ca.utoronto.fitbook.application.exceptions.EmptyQueryStringException;
import ca.utoronto.fitbook.application.port.in.LoadExerciseByBodyPartsPort;
import ca.utoronto.fitbook.application.port.in.LoadExerciseListByKeywordsPort;
import ca.utoronto.fitbook.application.port.in.LoadPostListByExerciseListPort;
import ca.utoronto.fitbook.application.port.in.SearchPostsUseCase;
import ca.utoronto.fitbook.application.port.in.command.SearchCommand;
import ca.utoronto.fitbook.application.port.out.response.SearchResponse;
import ca.utoronto.fitbook.entity.Exercise;
import ca.utoronto.fitbook.entity.Post;
import com.google.firebase.database.utilities.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService implements SearchPostsUseCase {

    private final LoadExerciseListByKeywordsPort loadExerciseListByKeywordsPort;
    private final LoadPostListByExerciseListPort loadPostListByExerciseList;
    private final LoadExerciseByBodyPartsPort loadExerciseByBodyPartsPort;

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
        List<Exercise> exerciseList = new ArrayList<>(loadExerciseByBodyPartsPort.loadExerciseByBodyParts(keywordsAndBodyParts));
        exerciseList.addAll(loadExerciseListByKeywordsPort.loadExerciseListByKeywords(keywordsAndBodyParts));

        //Loads posts based on exercises
        List<Post> postList = loadPostListByExerciseList.loadPostListByExerciseList(exerciseList.stream().map(Exercise::getId).collect(Collectors.toList()));

        //Calculates similarity between query string and post descriptions
        List<Pair<Post, Double>> weightedPostList = postList.stream().map(post -> new Pair<>(post, findSimilarity(queryString, post.getDescription()))).collect(Collectors.toList());

        //Sorts query results by calculated weight
        Comparator<Pair<Post, Double>> compareByWeight = (Pair<Post, Double> p1, Pair<Post, Double> p2) -> (int) Math.ceil((p1.getSecond() - p2.getSecond()) * 10);
        weightedPostList.sort(compareByWeight);

        return new SearchResponse(weightedPostList.stream().map(Pair::getFirst).collect(Collectors.toList()));
    }

    private List<String> extractKeywords(String queryString) {
        String[] words = queryString.split(" ");
        return Arrays.asList(words);
    }

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
        double quotient = dotProduct/(descriptionVectorMag * queryVectorMag);
        //Calculates cosine similarity
        return Math.cos(quotient);
    }

    private double calculateDotProduct(int[] vector1, int[] vector2) {
        int sum = 0;
        for(int i = 0; i < vector1.length; i++) {
            sum += vector1[i] * vector2[i];
        }
        return sum;
    }
}
