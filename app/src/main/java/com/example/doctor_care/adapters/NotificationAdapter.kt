package com.example.doctor_care.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doctor_care.models.Notification
import com.example.doctor_care.R
import com.example.doctor_care.databinding.NotificationItemBinding

class NotificationAdapter(
    private val notificatioList: ArrayList<Notification>,
    private val context: Context
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(val binding: NotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationAdapter.NotificationViewHolder {
        val view =
            NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationAdapter.NotificationViewHolder(view)

    }

    override fun onBindViewHolder(
        holder: NotificationAdapter.NotificationViewHolder,
        position: Int
    ) {
        val notification = notificatioList[position]
        holder.binding.apply {
            notificationTitle.text = notification.title
            notificationText.text = notification.message
            notificationDate.text = notification.date
            when (notification.categoryId) {
                "1nxhqrWkakTUoat1kQcX" -> {
                    Glide.with(context)
                        .load("https://firebasestorage.googleapis.com/v0/b/doctor-care-322ae.appspot.com/o/categorie%2FNutritionniste.png?alt=media&token=c04386b5-673e-4835-adea-c6274a51de70")
                        .into(categoryImage)
                    leftBar.setBackgroundColor(context.resources.getColor(R.color.basicGreen))
                    rightBar.setBackgroundColor(context.resources.getColor(R.color.basicGreen))
                    true
                }
                "HS5S3Pl501RWrps6kml0" -> {
                    Glide.with(context)
                        .load("https://firebasestorage.googleapis.com/v0/b/doctor-care-322ae.appspot.com/o/categorie%2FNeurologie.png?alt=media&token=66dae6b8-ee34-49f5-8511-a27dd9f9d9b9")
                        .into(categoryImage)

                    leftBar.setBackgroundColor(context.resources.getColor(R.color.basicOrange))
                    rightBar.setBackgroundColor(context.resources.getColor(R.color.basicOrange))
                    true
                }
                "Iwm9Acjf5y7H5pSU935MIwm9Acjf5y7H5pSU935M" -> {
                    Glide.with(context)
                        .load("https://firebasestorage.googleapis.com/v0/b/doctor-care-322ae.appspot.com/o/categorie%2Fgeneral.png?alt=media&token=4451fb52-e7ec-43bb-99a8-a187592645af")
                        .into(categoryImage)

                    leftBar.setBackgroundColor(context.resources.getColor(R.color.basicBlue))
                    rightBar.setBackgroundColor(context.resources.getColor(R.color.basicBlue))
                    true
                }
                "cuMYX434VlTMExGlBxDZ" -> {
                    Glide.with(context)
                        .load("https://firebasestorage.googleapis.com/v0/b/doctor-care-322ae.appspot.com/o/categorie%2Furologist.png?alt=media&token=b147121a-8e2d-4843-a846-d50b86d54e9e")
                        .into(categoryImage)

                    leftBar.setBackgroundColor(context.resources.getColor(R.color.basicGreen))
                    rightBar.setBackgroundColor(context.resources.getColor(R.color.basicGreen))
                    true
                }
                "jYBlz8d6HtpzcB8r0zCp" -> {
                    Glide.with(context)
                        .load("https://firebasestorage.googleapis.com/v0/b/doctor-care-322ae.appspot.com/o/categorie%2Fdiabitologist.png?alt=media&token=71d875a7-7f54-4183-9523-8cf38b9844c4")
                        .into(categoryImage)

                    leftBar.setBackgroundColor(context.resources.getColor(R.color.basicYellow))
                    rightBar.setBackgroundColor(context.resources.getColor(R.color.basicYellow))
                    true
                }
                "kUGirdhlQFtwtaKU6gjm" -> {
                    Glide.with(context)
                        .load("https://firebasestorage.googleapis.com/v0/b/doctor-care-322ae.appspot.com/o/categorie%2Fcardio.png?alt=media&token=ff275931-0c74-4ce1-ad18-636fcb651349")
                        .into(categoryImage)
                    leftBar.setBackgroundColor(context.resources.getColor(R.color.basicOrange))
                    rightBar.setBackgroundColor(context.resources.getColor(R.color.basicOrange))
                        true
                }
                "lj2mVh6zB0Tc68KDYbWa" -> {
                    Glide.with(context)
                        .load("https://firebasestorage.googleapis.com/v0/b/doctor-care-322ae.appspot.com/o/categorie%2Fherboriste.png?alt=media&token=fcf9b6e8-8376-4314-9c14-0c2a48ec16cc")
                        .into(categoryImage)

                    leftBar.setBackgroundColor(context.resources.getColor(R.color.basicGreen))
                    rightBar.setBackgroundColor(context.resources.getColor(R.color.basicGreen))
                    true
                }
                "yBluOth0aSr0Ue2JxXU7" -> {
                    Glide.with(context)
                        .load("https://firebasestorage.googleapis.com/v0/b/doctor-care-322ae.appspot.com/o/categorie%2Fherboriste.png?alt=media&token=fcf9b6e8-8376-4314-9c14-0c2a48ec16cc")
                        .into(categoryImage)
                    leftBar.setBackgroundColor(context.resources.getColor(R.color.basicBlue))
                    rightBar.setBackgroundColor(context.resources.getColor(R.color.basicBlue))
                    true
                }
                else -> {
                    Glide.with(context)
                        .load("https://firebasestorage.googleapis.com/v0/b/doctor-care-322ae.appspot.com/o/categorie%2FNutritionniste.png?alt=media&token=c04386b5-673e-4835-adea-c6274a51de70")
                        .into(categoryImage)

                    leftBar.setBackgroundColor(context.resources.getColor(R.color.basicPink))
                    rightBar.setBackgroundColor(context.resources.getColor(R.color.basicPink))
                    true
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return notificatioList.size
    }
}