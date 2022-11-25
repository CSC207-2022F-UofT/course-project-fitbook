package ca.utoronto.fitbook.adapter.persistence.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@ConfigurationProperties("fitbook")
public class FirebaseConfiguration {

    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${fitbook.config-file}")
    private String configFile;

    @Bean
    public Firestore getFirestore(){
        return FirestoreClient.getFirestore();
    }

    @PostConstruct
    public void init() {
        try {
            InputStream serviceAccount = new FileInputStream(configFile);
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(databaseUrl)
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e)  {
            System.err.println("Error: invalid database credentials.");
            throw new RuntimeException(e);
        }
    }
}
