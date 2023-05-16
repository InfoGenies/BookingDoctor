package com.example.doctor_care.views

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.doctor_care.R
import com.example.doctor_care.utils.Constante.Companion.LOADING_ANNOTATION
import com.example.doctor_care.utils.helper.Resource
import com.example.doctor_care.viewModel.GoogleAuthViewModel
import com.example.doctor_care.databinding.ActivityGooleAuthentificationBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named
@AndroidEntryPoint
class GooleAuthentification : AppCompatActivity() {
    private lateinit var binding: ActivityGooleAuthentificationBinding
    private val googleAuthViewModel: GoogleAuthViewModel by viewModels()
    @Inject
    lateinit var googleSignInOptions: GoogleSignInOptions

    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGooleAuthentificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeListener()
        configureGoogleSignIn()

    }

    private fun observeListener() {
        googleAuthViewModel.googleAuthLiveData.observe(this@GooleAuthentification, Observer{userState->
            when(userState){
                is Resource.Loading-> loadingDialog.show()
                is Resource.Success->{
                    navigateToMainFragment()
                    loadingDialog.hide()
                }
                is Resource.Error->{
                    loadingDialog.hide()
                    Toast.makeText(
                        this@GooleAuthentification,
                        "${userState.msg!!}",
                        Toast.LENGTH_SHORT
                    ).show()
///                    closeFragment()
                }
            }
        })
    }

    private fun navigateToMainFragment() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }

    // start google authentication intent to show all google save accounts to choose an account to sign in with it.
    private fun configureGoogleSignIn() {
        val mGoogleSignInClient = GoogleSignIn.getClient(
            this,
            googleSignInOptions
        )
        val signInIntent = mGoogleSignInClient.signInIntent
        googleLauncher.launch(signInIntent)
    }


    private val googleLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data: Intent? = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            googleAuthViewModel.handleGoogleAuthRequest(task,getString(R.string.errorMessage))
        }

}