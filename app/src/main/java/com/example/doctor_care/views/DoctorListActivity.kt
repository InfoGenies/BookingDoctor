package com.example.doctor_care.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.example.doctor_care.adapters.DoctorAdapter
import com.example.doctor_care.models.Doctors
import com.example.doctor_care.viewModel.DatafirestoreViewModel
import com.example.doctor_care.databinding.ActivityDoctorListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DoctorListActivity : AppCompatActivity(), DoctorAdapter.IDoctorListener {
    private lateinit var binding: ActivityDoctorListBinding
    private val viewModel: DatafirestoreViewModel by viewModels()
    lateinit var doctorAdapter: DoctorAdapter
    lateinit var animationView: LottieAnimationView

    private lateinit var List: ArrayList<Doctors>

    private var category_id: String = ""
    private var category_name: String = ""
    private var long: Double = 0.0
    private var latit: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        get_Intent()
        iniliazeView()
        List = ArrayList()
        viewModel.getDoctors(category_name)

        GlobalScope.launch(Dispatchers.Main) {

            delay(1500)
        //    Insert()
            viewModel.DocList.observe(this@DoctorListActivity, Observer { list ->
                val doctor: Doctors
                if (list.size == 0) {
                    hidelayout()
                } else {
                    for (i in 0..list.size - 1) {
                        val dist = distance(latit, long, list.get(i).lati, list.get(i).long)
                        println("la distance Mille deux point est ${dist}")

                        val km: Int = (dist / 0.62137).toInt()
                        println("la distance entre deux point est ${km}")

                        if (km < 4) {
                            list.get(i).distance = km.toString()
                            List.add(list.get(i))
                        }
                    }
                    showlayout()
                    SetupRv()
                }
            })
        }
        binding.appBarMain.back.setOnClickListener {
            this.finish()
        }
    }

    private fun iniliazeView() {
        animationView = binding.animationViewCartPage
    }

    private fun showlayout() {
        binding.apply {
            emptyBagMsgLayout.visibility = View.GONE
            animationView.pauseAnimation()
        }
    }

    private fun hidelayout() {
        binding.apply {
            animationView.playAnimation()
            emptyBagMsgLayout.visibility = View.VISIBLE

        }
    }

    private fun SetupRv() {

        doctorAdapter = DoctorAdapter(this@DoctorListActivity, List, this)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@DoctorListActivity)
            hasFixedSize()
            adapter = doctorAdapter
        }


    }

    private fun get_Intent() {
        category_id = intent.extras!!.getString("categoryId")!!
        category_name = intent.extras!!.getString("categoryName")!!
        long = intent.extras!!.getDouble("long")!!
        latit = intent.extras!!.getDouble("latit")!!
    }

    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    override fun onDoctorClick(doctor: Doctors) {
        val intent = Intent(this, BookAppointmentActivity::class.java)
        intent.putExtra("doctorObj", doctor)
        startActivity(intent)
    }

    /*fun Insert() {
        viewModel.insertDoctorData()
    }
*/
}