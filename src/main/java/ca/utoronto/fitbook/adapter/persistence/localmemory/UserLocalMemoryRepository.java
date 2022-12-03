package ca.utoronto.fitbook.adapter.persistence.localmemory;

import ca.utoronto.fitbook.adapter.persistence.GenericRepository;
import ca.utoronto.fitbook.application.exceptions.EntityNotFoundException;
import ca.utoronto.fitbook.application.exceptions.UsernameCollisionException;
import ca.utoronto.fitbook.application.exceptions.UsernameNotFoundException;
import ca.utoronto.fitbook.application.port.in.FindUserByNamePort;
import ca.utoronto.fitbook.application.port.in.LoadUserByNamePort;
import ca.utoronto.fitbook.application.port.in.LoadUserListPort;
import ca.utoronto.fitbook.application.port.in.LoadUserPort;
import ca.utoronto.fitbook.application.port.out.SaveUserPort;
import ca.utoronto.fitbook.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserLocalMemoryRepository implements GenericRepository<User>,
        LoadUserPort,
        LoadUserByNamePort,
        FindUserByNamePort,
        SaveUserPort,
        LoadUserListPort
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
     * @param name Name of user to fetch
     * @return If the user exists, return the user
     * @throws UsernameCollisionException If there are more than one users with the same name
     * @throws UsernameNotFoundException If the user name can't be found
     */
    @Override
    public User loadUserByName(String name) throws UsernameCollisionException, UsernameNotFoundException {
        List<User> users = new ArrayList<>();
        for (User user : datastore.values()) {
            if (user.getName().equals(name))
                users.add(user);
        }

        if (users.size() == 0)
            throw new UsernameNotFoundException(name);

        if (users.size() > 1)
            throw new UsernameCollisionException(name);

        return users.get(0);
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
     * @param user The user to be saved
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
