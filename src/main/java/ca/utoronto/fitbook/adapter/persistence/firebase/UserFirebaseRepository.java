package ca.utoronto.fitbook.adapter.persistence.firebase;

import ca.utoronto.fitbook.adapter.persistence.GenericRepository;
import ca.utoronto.fitbook.application.exceptions.EntityNotFoundException;
import ca.utoronto.fitbook.application.exceptions.UsernameCollisionException;
import ca.utoronto.fitbook.application.exceptions.UsernameNotFoundException;
import ca.utoronto.fitbook.application.port.in.*;
import ca.utoronto.fitbook.application.port.out.SaveUserPort;
import ca.utoronto.fitbook.entity.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
@RequiredArgsConstructor
public class UserFirebaseRepository implements GenericRepository<User>, LoadUserPort, LoadUserByNamePort, FindUserByNamePort, SaveUserPort {

    private final Firestore firestore;

    private static final String COLLECTION_NAME = "users";

    /**
     * @param id Id of the user
     * @return the user with the given Id
     * @throws EntityNotFoundException If the user can't be found by Id
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
     * @param id of user to be deleted
     */
    @Override
    public void delete(String id) {
        firestore.collection(COLLECTION_NAME).document(id).delete();
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
     * @param user The user to be saved
     */
    @Override
    public void saveUser(User user) {
        save(user);
    }

    /**
     * @param name Name of user to fetch
     * @return If the user exists, return the user
     * @throws UsernameCollisionException If there are more than one users with the same name
     * @throws UsernameNotFoundException If the user name can't be found
     */
    @Override
    public User loadUserByName(String name) throws UsernameCollisionException, UsernameNotFoundException {
        ApiFuture<QuerySnapshot> future = firestore.collection("users").whereEqualTo("name", name).get();
        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            if (documents.size() > 1)
                throw new UsernameCollisionException(name);
            if (documents.size() == 1)
                return documents.get(0).toObject(User.class);
            throw new UsernameNotFoundException(name);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param name Name of user to find
     * @return Whether the user exists
     */
    @Override
    public boolean findByName(String name) {
        try {
            loadUserByName(name);
            return true;
        } catch (UsernameNotFoundException | UsernameCollisionException e) {
            return false;
        }
    }

}
