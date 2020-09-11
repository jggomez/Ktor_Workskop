package co.devhack.di

import co.devhack.data.FirebaseDataSource
import co.devhack.data.PostRepositoryImp
import co.devhack.usecases.repositories.PostRepository
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import org.koin.dsl.module
import org.koin.experimental.builder.singleBy
import java.io.FileInputStream

const val ENV_FIREBASE_CREDENTIALS = "GOOGLE_APPLICATION_CREDENTIALS"

val postRepositoryModule = module(createdAtStart = true) {
    single {
        val firebaseCredentials = System.getenv(ENV_FIREBASE_CREDENTIALS) ?: ""
        val options = FirebaseOptions
            .builder()
            .setCredentials(GoogleCredentials.fromStream(FileInputStream(firebaseCredentials)))
            .build()
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options)
        }
        FirebaseDataSource(FirestoreClient.getFirestore())
    }

    singleBy<PostRepository, PostRepositoryImp>()
}