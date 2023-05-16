package com.example.doctor_care.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Doctors(
    val id :String ="",
    val category_id :String = "",
    val name :String = "",
    val category: String ="",
    var distance: String ="",
    val addres :String = "",
    val Rating :String ="",
    val image :String = "",
    val lati :Double = 0.0,
    val long :Double = 0.0
): Parcelable
