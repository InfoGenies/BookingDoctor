package com.example.doctor_care.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.doctor_care.databinding.ActivitySuccessfulBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SuccessfulActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySuccessfulBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessfulBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    override fun onBackPressed() {
        val Intent = Intent(this@SuccessfulActivity, MainActivity::class.java)
        startActivity(Intent)
    }
}