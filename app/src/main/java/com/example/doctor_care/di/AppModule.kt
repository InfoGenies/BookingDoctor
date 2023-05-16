package com.example.doctor_care.di

import android.content.Context
import com.example.doctor_care.R
import com.example.doctor_care.repository.DataFirestoreRepo
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideFireStoreInstance(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
    @Singleton
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Singleton
    @Provides
    fun provideGoogleSignInOptions(@ApplicationContext context: Context): GoogleSignInOptions {
        return   GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            // dont worry about this error
            .requestEmail()
            .build()
        // default_web_client_id are auto-generated

    }


    @Provides
    @Singleton
    fun getContext(@ApplicationContext context: Context):Context{
        return context
    }

    @Provides
    @Singleton
    fun ProvidRepository(firebaseDB: FirebaseFirestore,context: Context): DataFirestoreRepo {
        return DataFirestoreRepo(firebaseDB,context)
    }

}