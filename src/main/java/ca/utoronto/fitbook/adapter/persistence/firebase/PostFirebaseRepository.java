package ca.utoronto.fitbook.adapter.persistence.firebase;

import ca.utoronto.fitbook.adapter.persistence.GenericRepository;
import ca.utoronto.fitbook.application.exceptions.EntityNotFoundException;
import ca.utoronto.fitbook.application.port.in.LoadPaginatedPosts;
import ca.utoronto.fitbook.application.port.in.LoadPostListPort;
import ca.utoronto.fitbook.application.port.in.LoadPostPort;
import ca.utoronto.fitbook.application.port.out.SavePostPort;
import ca.utoronto.fitbook.entity.Post;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
@RequiredArgsConstructor
public class PostFirebaseRepository
        extends GenericFirebaseRepository
        implements GenericRepository<Post>,
        LoadPostPort,
        SavePostPort,
        LoadPostListPort,
        LoadPaginatedPosts
{

    private static final String COLLECTION_NAME = "posts";
    private final Firestore firestore;

    @Override
    protected Firestore getFirestore() {
        return firestore;
    }

    @Override
    protected String getCollectionName() {
        return COLLECTION_NAME;
    }


    /**
     * @param id Id of the post
     * @return the post with the given Id
     * @throws EntityNotFoundException If the post doesn't exist
     */
    @Override
    public Post getById(String id) throws EntityNotFoundException {
        return getDocumentById(id).toObject(Post.class);
    }

    /**
     * @param entity Post to be saved
     */
    @Override
    public void save(Post entity) {
        try {
            ApiFuture<WriteResult> future = firestore.collection(COLLECTION_NAME).document(entity.getId()).set(entity);
            // Make the save synchronous
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param id of post to be deleted
     */
    @Override
    public void delete(String id) {
        try {
            ApiFuture<WriteResult> future = firestore.collection(COLLECTION_NAME).document(id).delete();
            // Make delete synchronous
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param id Id of the post
     * @return the post with the given Id
     * @throws EntityNotFoundException If the post is not found
     */
    @Override
    public Post loadPost(String id) throws EntityNotFoundException {
        return getById(id);
    }

    /**
     * @param post Post to be saved
     */
    @Override
    public void savePost(Post post) {
        save(post);
    }

    /**
     * @param postIds The post ids to be fetched
     * @return A list of posts
     * @throws EntityNotFoundException If a single post is not found
     */
    @Override
    public List<Post> loadPostList(List<String> postIds) throws EntityNotFoundException {
        List<Post> postList = new ArrayList<>();
        List<DocumentSnapshot> postDocumentList = getDocumentList(postIds);
        for (DocumentSnapshot document : postDocumentList) {
            if (!document.exists())
                throw new EntityNotFoundException(document.getId());
            postList.add(document.toObject(Post.class));
        }
        return postList;
    }

    /**
     * Loads a maximum of `limit` number of posts after the paginationKey Id post
     *
     * @param paginationKey The post Id to begin the search at
     * @param limit         The maximum number of posts to return
     * @return A maximum of `limit` posts in a list
     */
    @Override
    public List<Post> loadPaginatedPosts(String paginationKey, int limit) {
        try {
            // Create a query to sort all posts by date and only fetch everything
            // after the key and only grab `limit` amount
            Query query = firestore.collection(COLLECTION_NAME)
                    .orderBy("postDate", Query.Direction.DESCENDING);

            // Use the pagination key if we have one
            if (paginationKey != null) {
                DocumentSnapshot paginationKeyPost = firestore.collection(COLLECTION_NAME).document(paginationKey).get().get();
                query = query.startAfter(paginationKeyPost);
            }

            ApiFuture<QuerySnapshot> future = query.limit(limit).get();

            // Collect all the posts from the query
            List<Post> posts = new ArrayList<>();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents)
                posts.add(document.toObject(Post.class));

            return posts;
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
