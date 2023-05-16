package com.example.doctor_care.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doctor_care.models.Category
import com.example.doctor_care.databinding.CateoryItemBinding

class CategoryAdapter(
    private val ctx: Context,
    private val categoryList: ArrayList<Category>,
    val listener: ICategoryListener
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(val binding: CateoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.CategoryViewHolder {
        val view = CateoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(view)

    }

    override fun onBindViewHolder(holder: CategoryAdapter.CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.binding.apply {
            Glide.with(ctx)
                .load(category.image)
                .into(categoryImage)
            categoryTitle.text = category.name
            subCategoryTitle.text = category.desc
            categoryLayout.setOnClickListener {
                listener.onCategoryClick(category.id, category.name)
            }

        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}

interface ICategoryListener {
    fun onCategoryClick(category_id: String, category_name: String)
}