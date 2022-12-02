package ca.utoronto.fitbook.adapter.persistence.localmemory;

import ca.utoronto.fitbook.adapter.persistence.GenericRepository;
import ca.utoronto.fitbook.application.exceptions.EntityNotFoundException;
import ca.utoronto.fitbook.application.port.in.LoadExerciseListPort;
import ca.utoronto.fitbook.entity.Exercise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExerciseLocalMemoryRepository implements GenericRepository<Exercise>, LoadExerciseListPort
{
    private static final Map<String, Exercise> datastore = new HashMap<>();

    /**
     * @param id Id of the exercise
     * @return the exercise with the given Id
     */
    @Override
    public Exercise getById(String id) throws EntityNotFoundException {
        if (!datastore.containsKey(id))
            throw new EntityNotFoundException(id);
        return datastore.get(id);
    }

    /**
     * @param entity The exercise to be saved
     */
    @Override
    public void save(Exercise entity) {
        datastore.put(entity.getId(), entity);
    }

    /**
     * @param id
     */
    @Override
    public void delete(String id) {
        datastore.remove(id);
    }

    /**
     * @param exerciseIds The exercise ids to be fetched
     * @return A list of exercises
     * @throws EntityNotFoundException If a single exercise is not found
     */
    @Override
    public List<Exercise> loadExerciseList(List<String> exerciseIds) throws EntityNotFoundException {
        List<Exercise> exerciseList = new ArrayList<>();
        for (String id : exerciseIds) {
            exerciseList.add(getById(id));
        }
        return exerciseList;
    }
}

