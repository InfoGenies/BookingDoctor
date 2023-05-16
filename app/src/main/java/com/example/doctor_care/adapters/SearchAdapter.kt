package com.example.doctor_care.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doctor_care.models.Doctors
import com.example.doctor_care.databinding.DoctorListItemBinding
import android.widget.Filter
import android.widget.Filterable
import kotlin.collections.ArrayList

class SearchAdapter(
    private val ctx: Context,

    val listener: IDoctorListener
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>(), Filterable {
    private var ListFiltred: ArrayList<Doctors> = ArrayList()
    private var doctorList: ArrayList<Doctors> = ArrayList()


    class SearchViewHolder(val binding: DoctorListItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchAdapter.SearchViewHolder {
        val view = DoctorListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchAdapter.SearchViewHolder, position: Int) {
        println(" la taille de doctorList est ${doctorList.size}")
        val doctor = ListFiltred[position]
        holder.binding.apply {
            Glide.with(ctx)
                .load(doctor.image)
                .into(categoryImage)
            doctorName.text = doctor.name
            categoryName.text = doctor.category
            address.text = doctor.addres
            doctorRating.rating = doctor.Rating.toFloat()
            distance.text = doctor.distance + " Km"
            bookBtn.setOnClickListener {
                listener.onDoctorClick(doctor)
            }
        }

    }

    override fun getItemCount(): Int {
        return ListFiltred.size
    }

    fun Update(DoctorListe: ArrayList<Doctors>) {
        ListFiltred.clear()
        doctorList = DoctorListe
        ListFiltred = doctorList
        notifyDataSetChanged()

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                println("la valeur de char est ${charString}")
                if (charString.isEmpty()) {
                    ListFiltred = doctorList
                } else {
                    val resultList = ArrayList<Doctors>()
                    println(" la taille de la listeDoctor est ${doctorList.size}")
                    doctorList
                        .filter {
                            (it.name.contains(charString))

                        }
                        .forEach { resultList.add(it) }
                    ListFiltred = resultList
                    println("la taille de la liste filtring est ${ListFiltred.size}")

                }
                return FilterResults().apply { values = ListFiltred }
            }

            override fun publishResults(p0: CharSequence?, results: FilterResults?) {
                ListFiltred = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<Doctors>
                notifyDataSetChanged()
            }

        }
    }
    interface IDoctorListener {
        fun onDoctorClick(doctor: Doctors)
    }

}






