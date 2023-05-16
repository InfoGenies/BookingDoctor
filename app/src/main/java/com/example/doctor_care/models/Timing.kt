package com.example.doctor_care.models

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Timing(
    val timeSlot: String = "",
    val time: String = "",
    val order: Int = 0
) {
}
