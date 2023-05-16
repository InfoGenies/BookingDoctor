package com.example.doctor_care.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doctor_care.models.Doctors
import com.example.doctor_care.databinding.DoctorListItemBinding

class DoctorAdapter(
    private val ctx: Context,
    private val doctorList: ArrayList<Doctors>,
    val listener: IDoctorListener
) : RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {


    class DoctorViewHolder(val binding: DoctorListItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DoctorAdapter.DoctorViewHolder {
        val view = DoctorListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DoctorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorAdapter.DoctorViewHolder, position: Int) {
        val doctor = doctorList[position]
        holder.binding.apply {
            Glide.with(ctx)
                .load(doctor.image)
                .into(categoryImage)
            doctorName.text = doctor.name
            categoryName.text = doctor.category
            address.text = doctor.addres
            doctorRating.rating = doctor.Rating.toFloat()
            distance.text = doctor.distance +" Km"
            bookBtn.setOnClickListener{
                listener.onDoctorClick(doctor)
            }
        }

    }

    override fun getItemCount(): Int {
        return doctorList.size
    }

    interface IDoctorListener {
        fun onDoctorClick(doctor: Doctors)
    }
}
