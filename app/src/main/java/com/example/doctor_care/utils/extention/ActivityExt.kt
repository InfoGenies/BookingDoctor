package com.example.doctor_care.utils.extention

import com.example.doctor_care.R
import com.example.doctor_care.views.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


fun MainActivity.showBottomNav(){
    val navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
    if(!navigation.isShown)
        navigation.show()
}
fun MainActivity.hideBottomNav(){
    val navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
    navigation.hide()
}
