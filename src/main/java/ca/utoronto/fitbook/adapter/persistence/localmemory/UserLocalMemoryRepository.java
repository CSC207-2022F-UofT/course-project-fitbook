package ca.utoronto.fitbook.adapter.persistence.localmemory;

import ca.utoronto.fitbook.adapter.persistence.GenericRepository;
import ca.utoronto.fitbook.application.exceptions.EntityNotFoundException;
import ca.utoronto.fitbook.application.port.in.LoadUserListPort;
import ca.utoronto.fitbook.application.port.in.LoadUserPort;
import ca.utoronto.fitbook.application.port.out.SaveUserPort;
import ca.utoronto.fitbook.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserLocalMemoryRepository implements GenericRepository<User>, LoadUserPort, SaveUserPort, LoadUserListPort
{
    private static final Map<String, User> datastore = new HashMap<>();

    /**
     * @param id Id of the user
     * @return the user with the given Id
     */
    @Override
    public User getById(String id) throws EntityNotFoundException {
        if (!datastore.containsKey(id))
            throw new EntityNotFoundException(id);
        return datastore.get(id);
    }

    /**
     * @param entity The user to be saved
     */
    @Override
    public void save(User entity) {
        datastore.put(entity.getId(), entity);
    }

    /**
     * @param id of user to be deleted
     */
    @Override
    public void delete(String id) {
        datastore.remove(id);
    }

    /**
     * @param id Id of the user
     * @return the user with the given Id
     */
    @Override
    public User loadUser(String id) throws EntityNotFoundException {
        return getById(id);
    }

    /**
     * @param user to be saved
     */
    @Override
    public void saveUser(User user) {
        save(user);
    }

    /**
     * @param userIds The user ids to be fetched
     * @return A list of users
     * @throws EntityNotFoundException If a single user is not found
     */
    @Override
    public List<User> loadUserList(List<String> userIds) throws EntityNotFoundException {
        List<User> userList = new ArrayList<>();
        for (String id : userIds) {
            userList.add(getById(id));
        }
        return userList;
    }
}
