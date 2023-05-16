package com.example.doctor_care.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.doctor_care.models.Appointment
import com.example.doctor_care.models.Doctors
import com.example.doctor_care.viewModel.DatafirestoreViewModel
import com.example.doctor_care.databinding.ActivityAppointmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Matcher
import java.util.regex.Pattern
@AndroidEntryPoint
class AppointmentDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppointmentDetailsBinding
    private val viewModel: DatafirestoreViewModel by viewModels()
    private lateinit var Appoint: Appointment
    private lateinit var doctor: Doctors
    private val VALID_EMAIL_ADDRESS_REGEX: Pattern =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.appBar.toolbarTitle.text = "Personel Detail"
        binding.appBar.back.setOnClickListener {
            finish()
        }
        binding.appBar.rightIcon?.visibility = View.INVISIBLE

        setupDoctorDetails()
        binding.DoneBtn.setOnClickListener {
            if (validateInputs()){
                viewModel.insertAppoint(Appoint)
                val intent = Intent(this@AppointmentDetailsActivity, SuccessfulActivity::class.java)
                startActivity(intent)
            }
        }


    }

    private fun setupDoctorDetails() {
        doctor = intent.extras!!.getParcelable<Doctors>("doctorObj")!!
        Appoint = intent.extras!!.getParcelable<Appointment>("AppointObj")!!
        binding.doctorDetails.apply {
            Glide.with(this@AppointmentDetailsActivity)
                .load(doctor.image)
                .into(categoryImage)
            doctorName.text = doctor.name
            categoryName.text = doctor.category
            distance.text = doctor.distance
            address.text = doctor.addres
            doctorRating.rating = doctor.Rating.toFloat()
        }

    }

    fun validateEmail(emailStr: String): Boolean {
        val matcher: Matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr)
        return matcher.find()
    }
    fun validateInputs():Boolean{
        if (binding.name.text.isEmpty()){
            binding.name.error = "Enter your Name"
            // Toast.makeText(this, "Enter Your Name", Toast.LENGTH_SHORT).show()
            return false
        }

        if (binding.mobile.text.isEmpty()){
            binding.mobile.error = "Enter Your Phone Number"
            //Toast.makeText(this, "Enter Your Phone Number", Toast.LENGTH_SHORT).show()
            return false
        }else if (binding.mobile.text.length != 10){
            binding.mobile.error = "Enter Your 10 Digit Phone Number"
            //Toast.makeText(this, "Enter Your 10 Digit Phone Number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.email.text.isEmpty()){
            binding.email.error = "Enter Your Email Id"
            //Toast.makeText(this, "Enter Your Email Id", Toast.LENGTH_SHORT).show()
            return false
        }else if (!validateEmail(binding.email.text.toString())){
            binding.email.error = "Enter Valid Email Id"
            //Toast.makeText(this, "Enter Valid Email Id", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.comments.text.isEmpty()){
            binding.comments.error = "Enter details of the illness"
            //Toast.makeText(this, "Enter details of the illness", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }


}