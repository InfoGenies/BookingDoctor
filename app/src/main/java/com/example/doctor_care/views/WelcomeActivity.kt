package com.example.doctor_care.views

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.doctor_care.viewModel.PhoneAuthViewModel
import com.example.doctor_care.databinding.ActivityWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private val authViewModel: PhoneAuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isFirstLogIn = authViewModel.checkIfFirstAppOpened()
        println(" la valeur de isFirstLogIn ${isFirstLogIn}")
        if(!isFirstLogIn){
            checkIfUserLoggedIn()
        }


    }

    private fun checkIfUserLoggedIn() {
        val isLoggedIn = authViewModel.checkIfUserLoggedIn()
        if(isLoggedIn){
            navigateToMainFragment()
        }else{
            navigateToAuthenticationFragment()
        }
    }

    private fun navigateToAuthenticationFragment() {
        val intent = Intent(this, Authentification::class.java)
        startActivity(intent)
    }

    private fun navigateToMainFragment() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }


}