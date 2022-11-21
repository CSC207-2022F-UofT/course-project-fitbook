package ca.utoronto.fitbook.adapter.persistence.firebase;

import ca.utoronto.fitbook.adapter.persistence.GenericRepository;
import ca.utoronto.fitbook.entity.Exercise;
import ca.utoronto.fitbook.entity.RepetitiveExercise;
import ca.utoronto.fitbook.entity.TemporalExercise;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Repository
public class ExerciseFirebaseRepository implements GenericRepository<Exercise>
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
    public Exercise getById(String id) {
        ApiFuture<DocumentSnapshot> future = datastore.getCollection("exercises").document(id).get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                String type = document.getString("type");
                if (Objects.equals(type, Exercise.ExerciseType.REPETITIVE.toString()))
                    return document.toObject(RepetitiveExercise.class);
                else if (Objects.equals(type, Exercise.ExerciseType.TEMPORAL.toString()))
                    return document.toObject(TemporalExercise.class);

                throw new InvalidExerciseTypeException(type);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * @param entity The exercise to be saved
     */
    @Override
    public void save(Exercise entity) {
        datastore.getCollection("exercises").document(entity.getId()).set(entity);
    }
}
