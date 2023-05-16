package com.example.doctor_care.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.doctor_care.R
import com.example.doctor_care.databinding.ActivityGetStartedBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Get_Started : AppCompatActivity() {
    private lateinit var binding: ActivityGetStartedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetStartedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.getStartedButton.setOnClickListener {

        }

    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.frame_id, fragment)
            .commit()

    }
}