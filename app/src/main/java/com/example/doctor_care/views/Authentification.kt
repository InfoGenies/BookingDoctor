package com.example.doctor_care.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.doctor_care.databinding.ActivityAuthentificationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Authentification : AppCompatActivity() {
    private lateinit var binding: ActivityAuthentificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthentificationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.countryCodePicker.setOnClickListener {
            navigateToAuthenticationPhone()
        }
        binding.authGoogleLayout.Gmail.setOnClickListener {
            avigateToAuthenticationGoogle()
        }


    }

    private fun navigateToAuthenticationPhone() {
        val intent = Intent(this, PhoneNumberAuth::class.java)
        startActivity(intent)
    }

    private fun avigateToAuthenticationGoogle() {
        val intent = Intent(this, GooleAuthentification::class.java)
        startActivity(intent)

    }

}