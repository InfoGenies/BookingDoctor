package com.example.doctor_care.utils.extention

import android.view.View
import android.view.animation.AnimationUtils

fun View.showWithAnimate(anim: Int){
    show()
    animation = AnimationUtils.loadAnimation(context, anim).apply {
        start()
    }
}

fun View.show() {
    visibility = View.VISIBLE
}
fun View.hide() {
    visibility = View.GONE
}