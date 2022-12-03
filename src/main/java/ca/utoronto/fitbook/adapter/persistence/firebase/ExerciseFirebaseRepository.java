package ca.utoronto.fitbook.adapter.persistence.firebase;

import ca.utoronto.fitbook.adapter.persistence.ExerciseTypeToClassMap;
import ca.utoronto.fitbook.adapter.persistence.GenericRepository;
import ca.utoronto.fitbook.application.exceptions.EntityNotFoundException;
import ca.utoronto.fitbook.application.port.in.LoadExerciseByBodyPartsPort;
import ca.utoronto.fitbook.application.port.in.LoadExerciseListByKeywordsPort;
import ca.utoronto.fitbook.application.port.in.LoadExerciseListPort;
import ca.utoronto.fitbook.entity.Exercise;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import lombok.RequiredArgsConstructor;
import com.google.cloud.firestore.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Repository
@RequiredArgsConstructor
public class ExerciseFirebaseRepository
        extends GenericFirebaseRepository
        implements GenericRepository<Exercise>,
        LoadExerciseListPort, LoadExerciseListByKeywordsPort, LoadExerciseByBodyPartsPort
{

    private static final String COLLECTION_NAME = "exercises";
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
     * @param keywords
     * @return
     */
    @Override
    public List<Exercise> loadExerciseListByKeywords(List<String> keywords) {

        try {
            List<QueryDocumentSnapshot> querySnapshot = firestore.collection(COLLECTION_NAME)
                    .whereArrayContainsAny("keywords", keywords)
                    .get()
                    .get()
                    .getDocuments();
            List<Exercise> exerciseList = new ArrayList<>();
            for (QueryDocumentSnapshot document : querySnapshot) {
                String type = document.getString("type");
                for (Exercise.ExerciseType exerciseType : Exercise.ExerciseType.values()) {
                    if (Objects.equals(type, exerciseType.toString())) {
                        String exerciseClassName = ExerciseTypeToClassMap.get(exerciseType);
                        exerciseList.add((Exercise) document.toObject(Class.forName(exerciseClassName)));
                    } else {
                        throw new InvalidExerciseTypeException(type);
                    }
                }
                exerciseList.add(document.toObject(Exercise.class));
            }
            return exerciseList;
        } catch (ClassNotFoundException | InterruptedException | ExecutionException e) {
            throw new RuntimeException();
        }
    }

    /**
     * @param bodyParts
     * @return
     */
    @Override
    public List<Exercise> loadExerciseByBodyParts(List<String> bodyParts) {
        try {
            List<QueryDocumentSnapshot> querySnapshot = firestore.collection(COLLECTION_NAME)
                    .whereArrayContainsAny("bodyParts", bodyParts)
                    .get()
                    .get()
                    .getDocuments();
            List<Exercise> exerciseList = new ArrayList<>();
            for (QueryDocumentSnapshot document : querySnapshot) {
                String type = document.getString("type");
                for (Exercise.ExerciseType exerciseType : Exercise.ExerciseType.values()) {
                    if (Objects.equals(type, exerciseType.toString())) {
                        String exerciseClassName = ExerciseTypeToClassMap.get(exerciseType);
                        exerciseList.add((Exercise) document.toObject(Class.forName(exerciseClassName)));
                    } else {
                        throw new InvalidExerciseTypeException(type);
                    }
                }
                exerciseList.add(document.toObject(Exercise.class));
            }
            return exerciseList;
        } catch (ClassNotFoundException | InterruptedException | ExecutionException e) {
            throw new RuntimeException();
        }
    }

    /**
     * @param id Id of the exercise
     * @return the exercise with the given Id
     */
    @Override
    public Exercise getById(String id) throws EntityNotFoundException {
        return documentToExercise(getDocumentById(id));
    }

    /**
     * @param entity The exercise to be saved
     */
    @Override
    public void save(Exercise entity) {
        try {
            ApiFuture<WriteResult> future = firestore.collection(COLLECTION_NAME).document(entity.getId()).set(entity);
            // Make the save synchronous
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param id of exercise to be deleted
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
     * @param exerciseIds The exercise ids to be fetched
     * @return A list of exercises
     * @throws EntityNotFoundException If a single exercise is not found
     */
    @Override
    public List<Exercise> loadExerciseList(List<String> exerciseIds) throws EntityNotFoundException {
        List<Exercise> exerciseList = new ArrayList<>();
        List<DocumentSnapshot> exerciseDocumentList = getDocumentList(exerciseIds);
        for (DocumentSnapshot document : exerciseDocumentList) {
            if (!document.exists())
                throw new EntityNotFoundException(document.getId());
            exerciseList.add(documentToExercise(document));
        }
        return exerciseList;
    }

    private Exercise documentToExercise(DocumentSnapshot document) {
        try {
            String type = document.getString("type");
            for (Exercise.ExerciseType exerciseType : Exercise.ExerciseType.values()) {
                if (Objects.equals(type, exerciseType.toString())) {
                    String exerciseClassName = ExerciseTypeToClassMap.get(exerciseType);
                    return (Exercise) document.toObject(Class.forName(exerciseClassName));
                }
            }
            throw new InvalidExerciseTypeException(type);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Invalid exercise type")
    private static class InvalidExerciseTypeException extends RuntimeException
    {
        public InvalidExerciseTypeException(String type) {
            super(String.format("Exercise type (%s) is invalid.", type));
        }
    }
}
