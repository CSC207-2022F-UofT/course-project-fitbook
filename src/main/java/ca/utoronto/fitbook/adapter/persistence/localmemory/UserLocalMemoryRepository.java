package ca.utoronto.fitbook.adapter.persistence.localmemory;

import ca.utoronto.fitbook.adapter.persistence.GenericRepository;
import ca.utoronto.fitbook.entity.User;

import java.util.HashMap;
import java.util.Map;

public class UserLocalMemoryRepository implements GenericRepository<User>
{
    private static final Map<String, User> datastore = new HashMap<>();

    /**
     * @param id Id of the user
     * @return the user with the given Id
     */
    @Override
    public User getById(String id) {
        return datastore.get(id);
    }

    /**
     * @param entity The user to be saved
     */
    @Override
    public void save(User entity) {
        datastore.put(entity.getId(), entity);
    }
}
