package datastore;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Datastore
{
    private static final String DATABASE_URL = "https://fitbook-746a5.us-east1.firebaseio.com/";
    private final Firestore database;
    private static volatile Datastore instance;

    public CollectionReference getCollection(String collection) {
        return database.collection(collection);
    }

    public static Datastore getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (Datastore.class) {
            if (instance == null) {
                instance = new Datastore();
            }
            return instance;
        }
    }

    private Datastore() {
        try {
            InputStream serviceAccount = new FileInputStream("fitbook.json");
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(DATABASE_URL)
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            System.err.println("Error: invalid database credentials.");
            throw new RuntimeException(e);
        }

        database = FirestoreClient.getFirestore();
    }

}
