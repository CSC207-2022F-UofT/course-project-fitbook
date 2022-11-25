package ca.utoronto.fitbook.adapter.persistence.firebase;

import ca.utoronto.fitbook.adapter.persistence.GenericRepository;
import ca.utoronto.fitbook.application.port.in.EntityNotFoundException;
import ca.utoronto.fitbook.application.port.in.FindUserByNamePort;
import ca.utoronto.fitbook.application.port.in.LoadUserByNamePort;
import ca.utoronto.fitbook.application.port.in.LoadUserPort;
import ca.utoronto.fitbook.application.port.out.SaveUserPort;
import ca.utoronto.fitbook.entity.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;

@Repository
public class UserFirebaseRepository implements GenericRepository<User>, LoadUserPort, LoadUserByNamePort, FindUserByNamePort, SaveUserPort {

    @Autowired
    private Firestore firestore;

    private static final String COLLECTION_NAME = "users";

    /**
     * @param id Id of the user
     * @return the user with the given Id
     */
    @Override
    public User getById(String id) throws EntityNotFoundException {
        ApiFuture<DocumentSnapshot> future = firestore.collection(COLLECTION_NAME).document(id).get();
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
        firestore.collection(COLLECTION_NAME).document(entity.getId()).set(entity);
    }

    /**
     * @param id Id of the user
     * @return the user with the given Id
     */
    @Override
    public User loadUser(String id) {
        return getById(id);
    }

    /**
     * @param name
     * @return
     */
    @Override
    public User loadUserByName(String name) {
        //TODO implement this method
        return null;
    }

    /**
     * @param userName
     * @return
     */
    @Override
    public boolean findByName(String userName) {
        //TODO implement this method
        return false;
    }
}
