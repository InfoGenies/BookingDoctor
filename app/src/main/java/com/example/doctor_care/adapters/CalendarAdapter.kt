package com.example.doctor_care.adapters

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.doctor_care.R
import com.example.doctor_care.utils.CalendarUtils
import com.example.doctor_care.databinding.CalendarCellBinding
import java.time.LocalDate

class CalendarAdapter(
    private var days: ArrayList<LocalDate?>,
    private val listener: ICalendarAdapter
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {


    class CalendarViewHolder(val binding: CalendarCellBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CalendarAdapter.CalendarViewHolder {
        val view = CalendarCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CalendarAdapter.CalendarViewHolder, position: Int) {
        val localDate = days[position]
        if (localDate == null) {
            holder.binding.cellDayText.text = ""
        } else {
            holder.binding.cellDayText.text = localDate.dayOfMonth.toString()
            if (localDate == CalendarUtils.selectedDate) {
                holder.binding.parentView.setBackgroundResource(R.drawable.background_shape_blue)
                holder.binding.cellDayText.setTextColor(Color.WHITE)
            }
            holder.binding.parentView.setOnClickListener {
                listener.onCalendarItemClicked(localDate)
            }

        }
    }

        override fun getItemCount(): Int {
            return days.size
        }


    interface ICalendarAdapter {
        fun onCalendarItemClicked(localDate: LocalDate)
    }

}
