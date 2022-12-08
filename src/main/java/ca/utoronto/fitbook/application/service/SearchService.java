package ca.utoronto.fitbook.application.service;

import ca.utoronto.fitbook.application.port.in.*;
import ca.utoronto.fitbook.application.port.in.command.SearchCommand;
import ca.utoronto.fitbook.application.port.out.response.PostResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

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


        //
        List<String> postIdList;
        try {
            postIdList = findSimilarity(queryTerms, postList);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        // maps postId to Post object for matching of sorted posts later
        HashMap<String, Post> postIdToPostMap = new HashMap<>();
        postList.forEach(post -> postIdToPostMap.put(post.getId(), post));

        //Sorted posts are retrieved by their id in their sorted order
        List<Post> sortedPostList = new ArrayList<>();
        for (String postId : postIdList) {
            sortedPostList.add(postIdToPostMap.get(postId));
        }
        List<Post> shortenedPostList = new ArrayList<>();

        // Limit search results to 10 posts
        for (int i = 0; i < sortedPostList.size() && i < 10; i++)
            shortenedPostList.add(sortedPostList.get(i));

        // Loads user to return
        User currentUser = loadUserPort.loadUser(searchCommand.getUserId());

        return new SearchResponse(PostListToPostResponseMapper.map(currentUser, shortenedPostList, loadExerciseListPort, loadUserListPort));
    }

    /**
     * Extract keywords out of string by splitting it
     *
     * @param queryString string to be split
     * @return list of strings
     */
    private List<String> extractKeywords(String queryString) {
        // Splits string by spaces
        String[] words = queryString.split(" ");
        return Arrays.asList(words);
    }

    /**
     * Finds similarity between query string and post descriptions
     *
     * @param queryTerms words that make up a query
     * @param postList list of posts to be queried
     * @return similarity between the two strings
     */
    private List<String> findSimilarity(List<String> queryTerms, List<Post> postList) throws IOException, ParseException {
        // Initializes analyzed and Buffer to interact with temporary directory
        StandardAnalyzer analyzer = new StandardAnalyzer();
        Directory index = new ByteBuffersDirectory();

        // Initilyzes index writer configuration so that strings can be indexed
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        IndexWriter w = new IndexWriter(index, config);

        // Add post description and ids as documents so that the description can be queried
        for (Post post : postList) {
            addDocument(w, post.getDescription(), post.getId());
        }

        // Closes access stream to documents
        w.close();
        int hitsPerPage = 10;

        // Opens and reads directory containing documents that contnain post description
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);

        // Searches documents and returns best matches to the query
        TopDocs docs = searcher.search(generateQueryFromQueryString(queryTerms), hitsPerPage);

        // Extract documents as and array and retrieve post ids to the most similar documents
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
        // Documents are created out of posts so they can be queried by lucene
        Document doc = new Document();
        doc.add(new TextField("description", description, Field.Store.YES));
        doc.add(new StringField("postId", postId, Field.Store.YES));
        writer.addDocument(doc);
    }

    private BooleanQuery generateQueryFromQueryString(List<String> queryTerms) {
        // Creates query used to searcg documents
        BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
        for (String term : queryTerms) {
            booleanQueryBuilder.add(new FuzzyQuery(new Term("description", term)), SHOULD);
        }
        return booleanQueryBuilder.build();
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST, reason = "Query cannot be empty.")
    public static class EmptyQueryStringException extends RuntimeException {
        public EmptyQueryStringException() {
            super("Query cannot be empty.");
        }
    }
}
