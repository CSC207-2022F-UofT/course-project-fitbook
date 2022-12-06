package ca.utoronto.fitbook.adapter.persistence.firebase;

import ca.utoronto.fitbook.adapter.persistence.GenericRepository;
import ca.utoronto.fitbook.application.exceptions.EntityNotFoundException;
import ca.utoronto.fitbook.application.exceptions.UserNotFoundException;
import ca.utoronto.fitbook.application.exceptions.UsernameCollisionException;
import ca.utoronto.fitbook.application.exceptions.UsernameNotFoundException;
import ca.utoronto.fitbook.application.port.in.CheckUserExistsPort;
import ca.utoronto.fitbook.application.port.in.FindUserByNamePort;
import ca.utoronto.fitbook.application.port.in.LoadUserByNamePort;
import ca.utoronto.fitbook.application.port.in.LoadUserListPort;
import ca.utoronto.fitbook.application.port.in.LoadUserPort;
import ca.utoronto.fitbook.application.port.out.SaveUserPort;
import ca.utoronto.fitbook.entity.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
@RequiredArgsConstructor
public class UserFirebaseRepository
        extends GenericFirebaseRepository
        implements GenericRepository<User>,
        LoadUserPort,
        LoadUserByNamePort,
        FindUserByNamePort,
        SaveUserPort,
        LoadUserListPort,
        CheckUserExistsPort
{

    private static final String COLLECTION_NAME = "users";
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
     * @param id Id of the user
     * @return the user with the given Id
     * @throws EntityNotFoundException If the user can't be found by Id
     */
    @Override
    public User getById(String id) throws EntityNotFoundException {
        return getDocumentById(id).toObject(User.class);
    }

    /**
     * @param entity The user to be saved
     */
    @Override
    public void save(User entity) {
        try {
            ApiFuture<WriteResult> future = firestore.collection(COLLECTION_NAME).document(entity.getId()).set(entity);
            // Make the save synchronous
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param id of user to be deleted
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
     * @param id Id of the user
     * @return the user with the given Id
     * @throws UserNotFoundException when user with given Id is not found
     */
    @Override
    public User loadUser(String id) {
        try {
            return getById(id);
        } catch (EntityNotFoundException e) {
            throw new UserNotFoundException(id);
        }
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
     * @throws UsernameNotFoundException  If the user name can't be found
     */
    @Override
    public User loadUserByName(String name) throws UsernameCollisionException, UsernameNotFoundException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).whereEqualTo("name", name).get();
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

    /**
     * @param userIds The user ids to be fetched
     * @return A list of users
     * @throws EntityNotFoundException If a single user is not found
     */
    @Override
    public List<User> loadUserList(List<String> userIds) throws EntityNotFoundException {
        List<User> userList = new ArrayList<>();
        List<DocumentSnapshot> userDocumentList = getDocumentList(userIds);
        for (DocumentSnapshot document : userDocumentList) {
            if (!document.exists())
                throw new EntityNotFoundException(document.getId());
            userList.add(document.toObject(User.class));
        }
        return userList;
    }

    /**
     *
     * @param userId Id of the user to find
     * @return Whether the user exists
     */
    @Override
    public boolean checkUserExists(String userId) {
        try {
            loadUser(userId);
            return true;
        } catch (UserNotFoundException e) {
            return false;
        }
    }
}
