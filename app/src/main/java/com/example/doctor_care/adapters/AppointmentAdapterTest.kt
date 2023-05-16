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

class AppointmentAdapterTest(
    private var timing: ArrayList<Timing>,
    private val listener: ITimeSlotLiteners,
    private val context: Context
) : RecyclerView.Adapter<AppointmentAdapterTest.AppointmentViewHolder>() {

    class AppointmentViewHolder(val binding: TimeItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var row_index: Int? = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = TimeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        Log.i("onCreateViewHolder", "call")
        return AppointmentViewHolder(view)

    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val Daytime = timing[position]
        holder.binding.appointTime.text = Daytime.time
        holder.binding.layoutId.setOnClickListener {
            if (row_index ==  holder.adapterPosition){
                row_index = RecyclerView.NO_POSITION
                notifyDataSetChanged()
                return@setOnClickListener
            }
            row_index = holder.adapterPosition
            Log.i("onBindViewHolder", "item holder clicked " + holder.adapterPosition)
            notifyDataSetChanged() // force calling onBindViewHolder Methode
            //   listener.onTimeSlotClicked(Daytime.time)

        }

        Log.i("onBindViewHolder", "numberIteration  " + holder.adapterPosition)
        if (row_index == position) {
            println("la valeur de la position est ${position}")
            holder.binding.appointTime.setBackgroundResource(R.drawable.background_shape_green)
            holder.binding.appointTime.setTextColor(Color.WHITE)
        } else {
            holder.binding.appointTime.setBackgroundResource(R.drawable.background_shape_white)
            holder.binding.appointTime.setTextColor(context.resources.getColor(R.color.basicGreen))
        }

    }

    override fun getItemCount(): Int {
        return timing.size
    }

    fun updateTimeSlotList(newList: ArrayList<Timing>) {
        timing.clear()
        timing = newList
        notifyDataSetChanged()

    }


}

interface ITimeSlotLiteners {
    fun onTimeSlotClickeds(time: String)
}