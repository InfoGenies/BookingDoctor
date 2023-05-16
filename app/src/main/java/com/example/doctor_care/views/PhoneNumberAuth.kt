package com.example.doctor_care.views

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.doctor_care.models.PhoneVerificationModel
import com.example.doctor_care.R
import com.example.doctor_care.utils.Constante.Companion.LOADING_ANNOTATION
import com.example.doctor_care.utils.helper.Resource
import com.example.doctor_care.utils.MainAuthState
import com.example.doctor_care.viewModel.PhoneAuthViewModel
import com.example.doctor_care.databinding.ActivityPhoneNumberAuthBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named
@AndroidEntryPoint
class PhoneNumberAuth : AppCompatActivity() {
    private lateinit var binding: ActivityPhoneNumberAuthBinding
    private val authViewModel: PhoneAuthViewModel by viewModels()
    private var verificationId: String? = null
    private var verificationToken: PhoneAuthProvider.ForceResendingToken? = null

    private var verificationTimeOut: Long = 0

    private var validPhoneNumber: String = ""

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhoneNumberAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.authByPhoneNumberFAB.setOnClickListener {
            checkPhoneNumber()
        }
        observeListener()

    }

    private fun observeListener() {
        authViewModel.phoneMainAuthLiveData.observe(this@PhoneNumberAuth, Observer {
            when (it) {
                /* some cases the phone number can be instantly verified without needing to send or enter a verification code
                    so here we will login with credential and we observe when login to open MainFragment if user has already an account
                    or open createUserFragment to add user info in app .
                 */
                is MainAuthState.SuccessWithCredential -> {
                    authViewModel.signInWithPhoneAuthCredential(it.data)
                    authViewModel.setPhoneAuthLiveData(MainAuthState.Idle)
                    loadingDialog.hide()
                }
                /*
                    Case two if user will verify with code has sent to him so here will open checkCodeAuth Fragment to check if
                    verification code has user added is correct .
                 */
                is MainAuthState.SuccessWithCode -> {
                    verificationId = it.verificationId
                    verificationToken = it.verificationToken
                    navigateToCheckPhoneNumberAuth(it.verificationId, it.verificationToken)
                    authViewModel.setPhoneAuthLiveData(MainAuthState.Idle)
                    loadingDialog.hide()

                }
                // If an error occurred will notify user with error message.
                is MainAuthState.Error -> {
                    loadingDialog.hide()
                    when (it.error) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            Toast.makeText(
                                this@PhoneNumberAuth,
                                "${getString(R.string.please_check_internet_connection)}",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                        else -> {
                            Toast.makeText(
                                this@PhoneNumberAuth,
                                "${getString(R.string.errorMessage)}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                // show loading dialog while send phone verification .
                is MainAuthState.Loading -> loadingDialog.show()
                // hide loading dialog when app wait reaction from user.
                is MainAuthState.Idle -> loadingDialog.hide()
            }
        })

        authViewModel.signInStatusLiveData.observe(this@PhoneNumberAuth, Observer {
            when (it) {
                // When had an error with automatically login app will push an error message.
                is Resource.Error -> {
                    loadingDialog.hide()
                    Toast.makeText(this@PhoneNumberAuth, "${it.msg!!}", Toast.LENGTH_SHORT).show()


                }
                /* Here we will login with credential and we observe when login to open MainFragment if user has already an account
                   or open createUserFragment to add user info in app .
                */
                is Resource.Success -> {
                    loadingDialog.hide()
                    navigateToMain()
                }
                is Resource.Loading -> loadingDialog.show()
            }
        })

    }

    fun checkPhoneNumber() {
        val phoneNumber = binding.phoneNumber.text.toString().trim()
        val countryCode = binding.countryCodePicker.selectedCountryCode
        when {
            phoneNumber.isEmpty() -> {
                Toast.makeText(
                    this@PhoneNumberAuth,
                    "${getString(R.string.please_add_your_phone_number_to_continue)}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            phoneNumber.toInt() < 4 -> {
                Toast.makeText(
                    this@PhoneNumberAuth,
                    "${getString(R.string.please_add_a_valid_phone_number)}",
                    Toast.LENGTH_SHORT
                ).show()

            }
            else -> {
                /* Check this is the first verification sent from this mobile and not a second one by check if firebase timeout
                   has finished . */
                validPhoneNumber = "+$countryCode$phoneNumber"
                if (verificationTimeOut == 0L || verificationTimeOut < System.currentTimeMillis()) {
                    sendFirstSMSVerification(validPhoneNumber)
                } else {
                    navigateToCheckPhoneNumberAuth(verificationId!!, verificationToken!!)
                }
            }
        }
    }

    private fun sendFirstSMSVerification(validPhoneNumber: String) {
        authViewModel.setPhoneAuthLiveData(MainAuthState.Loading)
        signInWithPhoneNumber(validPhoneNumber)
    }

    private fun signInWithPhoneNumber(validPhoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(validPhoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(authViewModel.phoneAuthCallBack())
            .build()
        verificationTimeOut = (System.currentTimeMillis() + 60000L)
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }

    private fun navigateToCheckPhoneNumberAuth(
        verificationId: String,
        verificationToken: PhoneAuthProvider.ForceResendingToken
    ) {
        val verificationModel =
            PhoneVerificationModel(verificationId, verificationToken, validPhoneNumber)

        val intent = Intent(this, phoneCodeCheckNumber::class.java)
        intent.putExtra("Obj", verificationModel)
        startActivity(intent)


    }


}