package ca.utoronto.fitbook.adapter.persistence.firebase;

import ca.utoronto.fitbook.application.exceptions.EntityNotFoundException;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
public abstract class GenericFirebaseRepository
{
    protected abstract Firestore getFirestore();
    protected abstract String getCollectionName();

    private ApiFuture<DocumentSnapshot> getFutureDocumentById(String id) {
        return getFirestore().collection(getCollectionName()).document(id).get();
    }

    /**
     * Gets a document snapshot from the database by id
     *
     * @param id The id of the document
     * @return Returns a snapshot of the document
     * @throws EntityNotFoundException Throws when the document is not found
     */
    protected DocumentSnapshot getDocumentById(String id) throws EntityNotFoundException {
        ApiFuture<DocumentSnapshot> future = getFutureDocumentById(id);
        try {
            DocumentSnapshot document = future.get();
            if (document.exists())
                return document;
            throw new EntityNotFoundException(id);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Fetches a list of document snapshots from the database by id asynchronously
     *
     * @param idList A list of ids to fetch from the database
     * @return Returns a list of document snapshots
     * @throws EntityNotFoundException Throws when one of the documents are not found
     */
    protected List<DocumentSnapshot> getDocumentList(List<String> idList) throws EntityNotFoundException {
        try {
            // Asynchronously load all the entities by Id
            List<ApiFuture<DocumentSnapshot>> futuresList = new ArrayList<>();
            for (String id : idList) {
                futuresList.add(getFutureDocumentById(id));
            }

            // Wait for all documents to resolve and return that list
            return ApiFutures.allAsList(futuresList).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
