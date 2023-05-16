package com.example.doctor_care.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pub(
    val id: String = "",
    val imagePub: String = ""
) : Parcelable
