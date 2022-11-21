package ca.utoronto.fitbook.adapter.persistence.firebase;

import ca.utoronto.fitbook.adapter.persistence.GenericRepository;
import ca.utoronto.fitbook.application.port.in.EntityNotFoundException;
import ca.utoronto.fitbook.application.port.in.LoadPostPort;
import ca.utoronto.fitbook.application.port.out.SavePostPort;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import ca.utoronto.fitbook.entity.Post;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;

@Repository
public class PostFirebaseRepository implements GenericRepository<Post>, LoadPostPort, SavePostPort
{
    private final FirebaseDatastore datastore;

    public PostFirebaseRepository() {
        this.datastore = FirebaseDatastore.getInstance();
    }

    /**
     * @param id Id of the post
     * @return the post with the given Id
     * @throws EntityNotFoundException If the post doesn't exist
     */
    @Override
    public Post getById(String id) throws EntityNotFoundException {
        ApiFuture<DocumentSnapshot> future = datastore.getCollection("posts").document(id).get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.toObject(Post.class);
            }
            throw new EntityNotFoundException(id);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param entity Post to be saved
     */
    @Override
    public void save(Post entity) {
        datastore.getCollection("posts").document(entity.getId()).set(entity);
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
}
