package com.example.doctor_care.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Appointment(
    var id: String = "",
    val doctorId: String = "",
    val categoryId: String = "",
    val date: String = "",
    val time: String = "",
): Parcelable
