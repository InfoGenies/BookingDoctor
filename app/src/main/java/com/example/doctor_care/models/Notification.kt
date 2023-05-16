package com.example.doctor_care.models

data class Notification(
    var id: String = "",
    val id_patient: String = "",
    val categoryId: String = "",
    val categoryName: String = "",
    val title: String = "",
    val message: String = "",
    val date: String = "",
)
