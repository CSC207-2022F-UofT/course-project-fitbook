package ca.utoronto.fitbook.adapter.persistence.firebase;

import ca.utoronto.fitbook.adapter.persistence.ExerciseTypeToClassMap;
import ca.utoronto.fitbook.adapter.persistence.GenericRepository;
import ca.utoronto.fitbook.application.port.in.EntityNotFoundException;
import ca.utoronto.fitbook.application.port.in.LoadExerciseListPort;
import ca.utoronto.fitbook.entity.Exercise;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Repository
public class ExerciseFirebaseRepository implements GenericRepository<Exercise>, LoadExerciseListPort
{
    private final FirebaseDatastore datastore;

    public ExerciseFirebaseRepository() {
        this.datastore = FirebaseDatastore.getInstance();
    }

    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Invalid exercise type")
    private static class InvalidExerciseTypeException extends RuntimeException {
        public InvalidExerciseTypeException(String type) {
            super(String.format("Exercise type (%s) is invalid.", type));
        }
    }

    /**
     * @param id Id of the exercise
     * @return the exercise with the given Id
     */
    @Override
    public Exercise getById(String id) throws EntityNotFoundException {
        ApiFuture<DocumentSnapshot> future = datastore.getCollection("exercises").document(id).get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                String type = document.getString("type");
                for (Exercise.ExerciseType exerciseType : Exercise.ExerciseType.values()) {
                    if (Objects.equals(type, exerciseType.toString())) {
                        String exerciseClassName = ExerciseTypeToClassMap.get(exerciseType);
                        return (Exercise) document.toObject(Class.forName(exerciseClassName));
                    }
                }
                throw new InvalidExerciseTypeException(type);
            }
            throw new EntityNotFoundException(id);
        } catch (InterruptedException | ExecutionException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param entity The exercise to be saved
     */
    @Override
    public void save(Exercise entity) {
        datastore.getCollection("exercises").document(entity.getId()).set(entity);
    }

    /**
     * @param exerciseIds The post ids to be fetched
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
