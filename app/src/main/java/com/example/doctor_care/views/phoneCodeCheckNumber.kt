package com.example.doctor_care.views

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.doctor_care.models.PhoneVerificationModel
import com.example.doctor_care.R
import com.example.doctor_care.utils.Constante.Companion.COUNT_DOWN_DELAY
import com.example.doctor_care.utils.Constante.Companion.COUNT_DOWN_INTERVAL
import com.example.doctor_care.utils.Constante.Companion.LOADING_ANNOTATION
import com.example.doctor_care.utils.helper.Resource
import com.example.doctor_care.viewModel.PhoneAuthViewModel
import com.example.doctor_care.databinding.ActivityPhoneNumberAuthenticationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class phoneCodeCheckNumber : AppCompatActivity() {
    private lateinit var binding: ActivityPhoneNumberAuthenticationBinding
    private val authViewModel: PhoneAuthViewModel by viewModels()

    private lateinit var verificationModel: PhoneVerificationModel
    private var isResendTextViewEnabled = false

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneNumberAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Get_Intent_verificationModel()
        binding.verifyOTPBtn.setOnClickListener {
            verifyPhoneNumber()
        }
        binding.resendTimerTextView.setOnClickListener {
            resendCode()
        }
        binding.resendCodeTextView.setOnClickListener {
            resendCode()
        }
        startMinuteCountDown()
        observeListener()

    }

    private fun Get_Intent_verificationModel() {
        verificationModel = intent.extras!!.getParcelable<PhoneVerificationModel>("Obj")!!

    }

    private fun startMinuteCountDown() {
        object : CountDownTimer(COUNT_DOWN_DELAY, COUNT_DOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                binding.resendTimerTextView.text =
                    getString(R.string.countDown, (millisUntilFinished / 1000))
            }

            override fun onFinish() {
                changeResendTextViewsStyle(false)
            }
        }.start()
    }

    fun changeResendTextViewsStyle(showTimer: Boolean) = with(binding) {
        if (showTimer) {
            resendTimerTextView.visibility = View.VISIBLE
            isResendTextViewEnabled = false
            resendCodeTextView.setTextColor(
                ContextCompat.getColor(
                    this@phoneCodeCheckNumber,
                    R.color.offWhite
                )
            )
        } else {
            resendTimerTextView.visibility = View.GONE
            isResendTextViewEnabled = true
            resendCodeTextView.setTextColor(
                ContextCompat.getColor(
                    this@phoneCodeCheckNumber,
                    R.color.green
                )
            )
        }
    }

    private fun observeListener() {
        authViewModel.signInStatusLiveData.observe(this@phoneCodeCheckNumber, Observer {
            when (it) {
                // When had an error with automatically login app will push an error message.
                is Resource.Error -> {
                    loadingDialog.hide()
                    Toast.makeText(
                        this@phoneCodeCheckNumber,
                        "${it.msg!!}",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                /* Here we will login with credential and we observe when login to open MainFragment if user has already an account
                   or open createUserFragment to add user info in app .
                */
                is Resource.Success -> {
                    loadingDialog.hide()
                    navigateToMainFragment()
                }
                is Resource.Loading -> {
                    loadingDialog.show()
                }
            }
        })
    }

    fun verifyPhoneNumber() {
        val smsCode =
            (binding.otpEditText1.text.toString() +
                    binding.otpEditText2.text.toString() +
                    binding.otpEditText3.text.toString() +
                    binding.otpEditText4.text.toString() +
                    binding.otpEditText5.text.toString() +
                    binding.otpEditText6.text.toString())

        if (smsCode.isEmpty()) {
            Toast.makeText(
                this@phoneCodeCheckNumber,
                "${getString(R.string.type_verification_code_first)}",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val credential =
                PhoneAuthProvider.getCredential(verificationModel.verificationId, smsCode)
            authViewModel.signInWithPhoneAuthCredential(credential)
        }
    }
    fun resendCode() {
        if (isResendTextViewEnabled) {
            changeResendTextViewsStyle(true)
            startMinuteCountDown()
        }
        resendVerificationCode()
    }


    private fun resendVerificationCode() {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(verificationModel.phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(authViewModel.phoneAuthCallBack())
            .setForceResendingToken(verificationModel.verificationToken)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun navigateToMainFragment() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }


}