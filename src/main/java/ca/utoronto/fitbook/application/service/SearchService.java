package ca.utoronto.fitbook.application.service;

import ca.utoronto.fitbook.application.exceptions.EmptyQueryStringException;
import ca.utoronto.fitbook.application.port.in.*;
import ca.utoronto.fitbook.application.port.in.command.SearchCommand;
import ca.utoronto.fitbook.application.port.out.response.SearchPostResponse;
import ca.utoronto.fitbook.application.port.out.response.SearchResponse;
import ca.utoronto.fitbook.entity.*;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.lucene.search.BooleanClause.Occur.SHOULD;

@Service
@RequiredArgsConstructor
public class SearchService implements SearchPostsUseCase {

    private final LoadExerciseListByKeywordsPort loadExerciseListByKeywordsPort;
    private final LoadPostListByExerciseListPort loadPostListByExerciseList;
    private final LoadExerciseByBodyPartsPort loadExerciseByBodyPartsPort;
    private final LoadExerciseListPort loadExerciseListPort;
    private final LoadUserPort loadUserPort;
    private final LoadUserListPort loadUserListPort;

    /**
     * @param searchCommand contains query string
     * @return SearchResponse object that contains posts relevant to query
     */
    @Override
    public SearchResponse search(SearchCommand searchCommand) {
        String queryString = searchCommand.getQueryString();

        // Checks if query string is not empty
        if (queryString.isEmpty())
            throw new EmptyQueryStringException();

        // Breaks up query string into individual words
        List<String> queryTerms = extractKeywords(queryString);

        // Queries exercise collection by keywords and body parts
        List<Exercise> queryExerciseList = new ArrayList<>(loadExerciseByBodyPartsPort.loadExerciseByBodyParts(queryTerms));
        queryExerciseList.addAll(loadExerciseListByKeywordsPort.loadExerciseListByKeywords(queryTerms));

        // Load posts based on exercises
        List<Post> postList;
        postList = loadPostListByExerciseList.loadPostListByExerciseList(queryExerciseList.stream()
                .map(Exercise::getId)
                .collect(Collectors.toList()));

        HashMap<String, Post> userIdToPost = new HashMap<>();
        postList.forEach(post -> userIdToPost.put(post.getAuthorId(), post));
        List<String> postIdList = new ArrayList<>();
        try {
            postIdList = findSimilarity4(queryTerms, postList);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        HashMap<String, Post> postIdToPostMap = new HashMap<>();
        postList.forEach(post -> postIdToPostMap.put(post.getId(), post));

        List<Post> sortedPostList = new ArrayList<>();
        for (String postId : postIdList) {
            sortedPostList.add(postIdToPostMap.get(postId));
        }
        List<Post> shortenedPostList = new ArrayList<>();

        // Limit search results to 10 posts
        for (int i = 0; i < sortedPostList.size() && i < 10; i++)
            shortenedPostList.add(sortedPostList.get(i));

        return new SearchResponse(findValuesForSearchPostResponseList(shortenedPostList));
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
     * @param queryTerms words that make up a query
     * @param postList list of posts to be queried
     * @return similarity between the two strings
     */
    private List<String> findSimilarity4(List<String> queryTerms, List<Post> postList) throws IOException, ParseException {
        StandardAnalyzer analyzer = new StandardAnalyzer();
        Directory index = new ByteBuffersDirectory();

        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        IndexWriter w = new IndexWriter(index, config);
        for (Post post : postList) {
            addDocument(w, post.getDescription(), post.getId());
        }
        w.close();

        int hitsPerPage = 10;
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(generateQueryFromQueryString(queryTerms), hitsPerPage);
        ScoreDoc[] hits = docs.scoreDocs;
        List<String> postIdList = new ArrayList<>();
        for (ScoreDoc hit : hits) {
            int docId = hit.doc;
            Document d = searcher.doc(docId);
            postIdList.add(d.get("postId"));
        }

        return postIdList;
    }

    private void addDocument(IndexWriter writer, String description, String postId) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("description", description, Field.Store.YES));
        doc.add(new StringField("postId", postId, Field.Store.YES));
        writer.addDocument(doc);
    }

    private BooleanQuery generateQueryFromQueryString(List<String> queryTerms) {
        BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
        for (String term : queryTerms) {
            booleanQueryBuilder.add(new FuzzyQuery(new Term("description", term)), SHOULD);
        }
        return booleanQueryBuilder.build();
    }
    private List<SearchPostResponse> findValuesForSearchPostResponseList(List<Post> postList) {

        // Initializes map and populates map of ExerciseIds to post
        HashMap<String, Post> postIdToPostMap = new HashMap<>();
        postList.forEach(post -> postIdToPostMap.put(post.getId(), post));

        // Initializes and populates map of users to userIds
        HashMap<String, Post> userIdToPost = new HashMap<>();
        postList.forEach(post -> userIdToPost.put(post.getAuthorId(), post));

        // Loads Exercises based on their ids
        List<String> exerciseIdList = postList.stream().flatMap(post -> post.getExerciseIdList().stream()).collect(Collectors.toList());
        List<Exercise> postExerciseList = loadExerciseListPort.loadExerciseList(exerciseIdList);

        // Retrieves repetitive exercises and converts them to RepetitiveExercise objects
        List<RepetitiveExercise> repetitiveExerciseList = postExerciseList.stream()
                .filter(exercise -> Exercise.ExerciseType.REPETITIVE.equals(exercise.getType()))
                .map(exercise -> (RepetitiveExercise) exercise)
                .collect(Collectors.toList());
        // Retrieves temporal exercises and converts them to TemporalExercise objects
        List<TemporalExercise> temporalExercises = postExerciseList.stream()
                .filter(exercise -> Exercise.ExerciseType.TEMPORAL.equals(exercise.getType()))
                .map(exercise -> (TemporalExercise) exercise)
                .collect(Collectors.toList());

        // Maps postIds to their related exercises
        HashMap<String, List<RepetitiveExercise>> postIdToRepetitiveExerciseMap = new HashMap<>();
        for (RepetitiveExercise exercise : repetitiveExerciseList) {
            String postId = exerciseIdToPostIdMap.get(exercise.getId());
            if (!postIdToRepetitiveExerciseMap.containsKey(postId)) {
                postIdToRepetitiveExerciseMap.put(postId, new ArrayList<>(List.of(exercise)));
            } else {
                postIdToRepetitiveExerciseMap.get(postId).add(exercise);
            }
        }

        HashMap<String, List<TemporalExercise>> postIdToTemporalExerciseMap = new HashMap<>();
        for (TemporalExercise exercise : temporalExercises) {
            String postId = exerciseIdToPostIdMap.get(exercise.getId());
            if (!postIdToTemporalExerciseMap.containsKey(postId)) {
                postIdToTemporalExerciseMap.put(postId, new ArrayList<>(List.of(exercise)));
            } else {
                postIdToTemporalExerciseMap.get(postId).add(exercise);
            }
        }

        List<User> userList = loadUserListPort.loadUserList(postList.stream()
                .map(Post::getAuthorId)
                .collect(Collectors.toList()));
        HashMap<String, User> userIdToUserMap = new HashMap<>();

        userList.forEach(user -> userIdToUserMap.put(user.getId(), user));

        List<SearchPostResponse> searchPostResponseList = new ArrayList<>();
        for (Post post : postList) {
            searchPostResponseList.add(new SearchPostResponse(
                    userIdToUserMap.get(post.getAuthorId()).getName(),
                    post,
                    Objects.requireNonNullElseGet(postIdToRepetitiveExerciseMap.get(post.getId()), ArrayList::new),
                    Objects.requireNonNullElseGet(postIdToTemporalExerciseMap.get(post.getId()), ArrayList::new)
            ));
        }

        return searchPostResponseList;
    }
}
