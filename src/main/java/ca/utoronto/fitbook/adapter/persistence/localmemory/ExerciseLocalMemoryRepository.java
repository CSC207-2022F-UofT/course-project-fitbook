package ca.utoronto.fitbook.adapter.persistence.localmemory;

import ca.utoronto.fitbook.adapter.persistence.GenericRepository;
import ca.utoronto.fitbook.entity.Exercise;

import java.util.HashMap;
import java.util.Map;

public class ExerciseLocalMemoryRepository implements GenericRepository<Exercise>
{
    private static final Map<String, Exercise> datastore = new HashMap<>();

    /**
     * @param id Id of the exercise
     * @return the exercise with the given Id
     */
    @Override
    public Exercise getById(String id) {
        return datastore.get(id);
    }

    /**
     * @param entity The exercise to be saved
     */
    @Override
    public void save(Exercise entity) {
        datastore.put(entity.getId(), entity);
    }
}
