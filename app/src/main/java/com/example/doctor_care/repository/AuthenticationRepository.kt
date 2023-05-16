package com.example.doctor_care.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.doctor_care.models.UserInfoModel
import com.example.doctor_care.models.convertMapToUserInfoModel
import com.example.doctor_care.models.toMap
import com.example.doctor_care.R
import com.example.doctor_care.utils.Constante.Companion.USERS_COLLECTION
import com.example.doctor_care.utils.helper.Resource
import com.example.doctor_care.utils.helper.SharedPreferenceHelper
import com.example.doctor_care.utils.MainAuthState
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationRepository
@Inject constructor(
    private val sharedPreferenceHelper: SharedPreferenceHelper,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage,
    private val firebaseFirestore: FirebaseFirestore,
    @ApplicationContext private val context: Context,
) {
    private val userUid by lazy { firebaseAuth.uid!! }
    private val firebaseUserCollection by lazy { firebaseFirestore.collection(USERS_COLLECTION) }


    fun checkIfFirstAppOpened(): Boolean = sharedPreferenceHelper.checkIfFirstAppOpened()

    fun checkIfUserLoggedIn(): Boolean {
        val user = firebaseAuth.currentUser
        return user != null
    }

    fun phoneAuthCallBack(_phoneMainAuthLiveData: MutableLiveData<MainAuthState>): PhoneAuthProvider.OnVerificationStateChangedCallbacks {
        return object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                _phoneMainAuthLiveData.value =
                    MainAuthState.SuccessWithCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                _phoneMainAuthLiveData.value = MainAuthState.Error(e)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                _phoneMainAuthLiveData.value =
                    MainAuthState.SuccessWithCode(verificationId, token)
            }
        }
    }

    suspend fun signInWithCredential(credential: AuthCredential): Resource<Unit?> {
        return try {
            firebaseAuth.signInWithCredential(credential).await()
            Resource.Success(null)
        } catch (e: Exception) {
            Resource.Error(context.getString(R.string.errorMessage))
        }
    }

    // check if user has data into firebase firestore or not.
    fun getUserInformation(userInfoLiveData: MutableLiveData<Resource<UserInfoModel>>) {
        firebaseFirestore.collection(USERS_COLLECTION).document(userUid).get()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null) {
                        if (document.exists()) {
                            val userInfoModel = convertMapToUserInfoModel(document.data!!)
                            userInfoLiveData.postValue(Resource.Success(userInfoModel))
                        } else {
                            userInfoLiveData.postValue(Resource.Error(context.getString(R.string.errorMessage)))
                        }
                    }
                } else {
                    Log.d("TAG", "Error: ", task.exception)
                }

            }
    }

    suspend fun changeUserLocation(location: String): Boolean {
        return try {
            firebaseFirestore.collection(USERS_COLLECTION).document(userUid).update(
                mapOf("userLocationName" to location)
            ).await()
            true
        } catch (e: Exception) {
            false
        }
    }


    suspend fun uploadUserInformation(
        userName: String,
        imageUri: Uri?,
        userLocation: String
    ): Resource<String> {
        return try {
            var accountStatusMessage = context.getString(R.string.accountCreatedSuccessfully)
            if (imageUri != null) {
                val uploadedImagePath = uploadUserImage(imageUri)
                val userInfoModel =
                    UserInfoModel(userUid, userName, uploadedImagePath, userLocation)
                firebaseUserCollection.document(userUid).set(userInfoModel.toMap()).await()
            } else {
                val userInfoModel =
                    UserInfoModel(userUid, userName, "", userLocation)
                firebaseUserCollection.document(userUid).update(userInfoModel.toMapWithoutImage())
                    .await()
                accountStatusMessage = context.getString(R.string.accountUpdatedSuccessfully)
            }
            Resource.Success(accountStatusMessage)
        } catch (e: Exception) {
            Log.d("TAG", e.message!!)
            Resource.Error(context.getString(R.string.errorCreateAccount))
        }

    }

    private suspend fun uploadUserImage(imageUri: Uri): String {
        val uploadingResult =
            firebaseStorage.reference.child("${USERS_COLLECTION}/${System.currentTimeMillis()}.jpg")
                .putFile(imageUri).await()
        return uploadingResult.metadata?.reference?.downloadUrl?.await().toString()
    }


}