package ca.utoronto.fitbook.adapter.persistence.firebase;

import ca.utoronto.fitbook.adapter.persistence.ExerciseTypeToClassMap;
import ca.utoronto.fitbook.adapter.persistence.GenericRepository;
import ca.utoronto.fitbook.application.exceptions.EntityNotFoundException;
import ca.utoronto.fitbook.application.port.in.LoadAllExercisesPort;
import ca.utoronto.fitbook.application.port.in.LoadExerciseByBodyPartsPort;
import ca.utoronto.fitbook.application.port.in.LoadExerciseListByKeywordsPort;
import ca.utoronto.fitbook.application.port.in.LoadExerciseListPort;
import ca.utoronto.fitbook.entity.Exercise;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.*;
import lombok.RequiredArgsConstructor;
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
        LoadExerciseListPort,
        LoadExerciseByBodyPartsPort,
        LoadExerciseListByKeywordsPort,
        LoadAllExercisesPort
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

    /**
     * @param keywords list of keywords
     * @return list of exercises
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
                    }
                }
            }
            return exerciseList;
        } catch (ClassNotFoundException | InterruptedException | ExecutionException e) {
            throw new RuntimeException();
        }
    }

    /**
     * @param bodyParts list of body parts
     * @return list of exercises
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
                    }
                }
            }
            return exerciseList;
        } catch (ClassNotFoundException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return A list of all exercises in the database
     */
    @Override
    public List<Exercise> loadAllExercises() {
        try {
            Iterable<DocumentReference> futureDocuments = firestore.collection(COLLECTION_NAME).listDocuments();
            List<ApiFuture<DocumentSnapshot>> futuresList = new ArrayList<>();
            for (DocumentReference documentReference : futureDocuments) {
                futuresList.add(documentReference.get());
            }

            List<Exercise> exerciseList = new ArrayList<>();
            for (DocumentSnapshot document : ApiFutures.allAsList(futuresList).get())
                exerciseList.add(documentToExercise(document));

            return exerciseList;
        } catch (InterruptedException | ExecutionException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper function to retrieve exercise object from document snapshot
     * @param document in database pointing to specific exercise
     * @return exercise located within document
     */
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
