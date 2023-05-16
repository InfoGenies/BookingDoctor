package com.example.doctor_care.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.doctor_care.models.Timing
import com.example.doctor_care.R
import com.example.doctor_care.databinding.TimeItemBinding

class AppointmentAdapter(
    private var timing: ArrayList<Timing>,
    private var appointmentList: ArrayList<String>,
    private val listener: ITimeSlotLitener,
    private val context: Context
) : RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {


    class AppointmentViewHolder(val binding: TimeItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var row_index: Int? = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = TimeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppointmentViewHolder(view)

    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        //val appointment = appointmentList[position]
        val Daytime = timing[position]
        holder.binding.apply {
            appointTime.text = Daytime.time
            layoutId.setOnClickListener {
                row_index = holder.adapterPosition
                Log.i("onBindViewHolder", "item holder clicked " + holder.adapterPosition)
                notifyDataSetChanged()
                listener.onTimeSlotClicked(Daytime.time)
            }
            Log.i("onBindViewHolder", "numberIteration  " + holder.adapterPosition)
            if (row_index == position) {
                holder.binding.appointTime.setBackgroundResource(R.drawable.background_shape_green)
                holder.binding.appointTime.setTextColor(Color.WHITE)
            } else {
                holder.binding.appointTime.setBackgroundResource(R.drawable.background_shape_white)
                holder.binding.appointTime.setTextColor(context.resources.getColor(R.color.basicGreen))
            }



            if (appointmentList.contains(Daytime.time)) {
                appointTime.setBackgroundResource(R.drawable.background_shape_red)
                appointTime.setTextColor(Color.WHITE)

            }

        }

    }

    override fun getItemCount(): Int {
        return timing.size
    }

    fun updateTimeSlotList(newList: ArrayList<Timing>) {
        row_index = -1
        timing.clear()
        timing = newList
        notifyDataSetChanged()

    }

}

interface ITimeSlotLitener {
    fun onTimeSlotClicked(time: String)
}