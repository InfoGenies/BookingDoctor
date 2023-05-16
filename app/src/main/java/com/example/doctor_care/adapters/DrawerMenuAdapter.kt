package com.example.doctor_care.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.doctor_care.R
import com.example.doctor_care.databinding.DrawerMenuItemBinding

class DrawerMenuAdapter(private val listener: IDrawerMenuAdapter) :
    RecyclerView.Adapter<DrawerMenuAdapter.MenuViewHolder>() {
    private val menulist: ArrayList<String> = ArrayList(4)
    private var selectedPosition: Int? = -1

    init {
        menulist.add("Home")
        menulist.add("Search")
        menulist.add("Notifications")
        menulist.add("Profile")
    }

    class MenuViewHolder(val binding: DrawerMenuItemBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return (MenuViewHolder(
            DrawerMenuItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ))
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = menulist[position]
        holder.binding.apply {
            menuText.text = menu
            when (menu) {
                "Home" -> {
                    menuImage.setImageResource(R.drawable.home)
                    true
                }
                "Search" -> {
                    menuImage.setImageResource(R.drawable.search)
                    true
                }
                "Notifications" -> {
                    menuImage.setImageResource(R.drawable.notification)
                    true
                }
                "Profile" -> {
                    menuImage.setImageResource(R.drawable.profile)
                    true
                }
                else -> {
                    menuImage.setImageResource(R.drawable.home)
                    true
                }
            }
            menuItem.setOnClickListener {
                if (selectedPosition ==  holder.adapterPosition){
                    selectedPosition = RecyclerView.NO_POSITION
                    notifyDataSetChanged()
                    return@setOnClickListener
                }
                selectedPosition = holder.adapterPosition
                notifyDataSetChanged()
                listener.onMenuItemClicked(position)
            }
            if (selectedPosition == position){
                menuItem.setBackgroundResource(R.drawable.background_shape_square_blue)
                menuText.setTextColor(Color.WHITE)
                menuImage.setColorFilter(Color.argb(255, 255, 255, 255));
            }else{
                menuItem.setBackgroundResource(R.drawable.background_shape_white)
                menuText.setTextColor(Color.BLACK)
                menuImage.setColorFilter(Color.argb(0, 0, 0, 0));
            }

        }

    }

    override fun getItemCount(): Int {
        return menulist.size
    }


}

interface IDrawerMenuAdapter {
    fun onMenuItemClicked(id: Int)
}
