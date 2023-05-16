package com.example.doctor_care.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doctor_care.R
import com.example.doctor_care.databinding.ViewpagerItemBinding

class ViewPager2Adapter(
    private val context: Context,
    private val appointmentAdapter: AppointmentAdapter,
    private val listener: IViewPagerListener
) : RecyclerView.Adapter<ViewPager2Adapter.PageViewHolder>() {
    private val timings = arrayOf(
        "Morning", "Afternoon", "Evening"
    )
    private val timeImages = intArrayOf(
        R.drawable.morning,
        R.drawable.afternoon,
        R.drawable.night,

        )

    class PageViewHolder(val binding: ViewpagerItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val view = ViewpagerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PageViewHolder(view)

    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.binding.apply {
            recyclerView.layoutManager = GridLayoutManager(context, 4)
            recyclerView.hasFixedSize()
            recyclerView.adapter = appointmentAdapter
            Log.i("position", "position=" + position)
            pageTitle.text = timings[position]
            pageTitleImage.setImageResource(timeImages[position])

            previous.setOnClickListener {
                listener.previousPage()
            }
            next.setOnClickListener {
                listener.nextPage()
            }

        }


    }

    override fun getItemCount(): Int {
        return timings.size
    }


}

interface IViewPagerListener {
    fun nextPage()
    fun previousPage()
}