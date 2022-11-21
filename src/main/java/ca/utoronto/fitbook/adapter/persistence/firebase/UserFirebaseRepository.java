package ca.utoronto.fitbook.adapter.persistence.firebase;

import ca.utoronto.fitbook.adapter.persistence.GenericRepository;
import ca.utoronto.fitbook.application.port.in.EntityNotFoundException;
import ca.utoronto.fitbook.application.port.in.LoadUserPort;
import ca.utoronto.fitbook.entity.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;

@Repository
public class UserFirebaseRepository implements GenericRepository<User>, LoadUserPort
{
    private final FirebaseDatastore datastore;

    public UserFirebaseRepository() {
        this.datastore = FirebaseDatastore.getInstance();
    }

    /**
     * @param id Id of the user
     * @return the user with the given Id
     */
    @Override
    public User getById(String id) throws EntityNotFoundException {
        ApiFuture<DocumentSnapshot> future = datastore.getCollection("users").document(id).get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.toObject(User.class);
            }
            throw new EntityNotFoundException(id);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param entity The user to be saved
     */
    @Override
    public void save(User entity) {
        datastore.getCollection("users").document(entity.getId()).set(entity);
    }

    /**
     * @param id Id of the user
     * @return the user with the given Id
     */
    @Override
    public User loadUser(String id) {
        return getById(id);
    }
}
