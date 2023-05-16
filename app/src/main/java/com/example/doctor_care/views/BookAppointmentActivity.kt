package com.example.doctor_care.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.doctor_care.adapters.*
import com.example.doctor_care.models.Appointment
import com.example.doctor_care.models.Doctors
import com.example.doctor_care.models.Timing
import com.example.doctor_care.utils.CalendarUtils
import com.example.doctor_care.viewModel.DatafirestoreViewModel
import com.example.doctor_care.databinding.ActivityBookAppointmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.collections.ArrayList

@AndroidEntryPoint
class BookAppointmentActivity : AppCompatActivity(), CalendarAdapter.ICalendarAdapter,
    ITimeSlotLitener, IViewPagerListener {
    private lateinit var binding: ActivityBookAppointmentBinding
    private val viewModel: DatafirestoreViewModel by viewModels()
    lateinit var appointmentAdapter: AppointmentAdapter
    lateinit var calendarAdapter: CalendarAdapter
    lateinit var viewPager2Adapter: ViewPager2Adapter
    private var Time: String = ""
    private lateinit var ListeFiltring: ArrayList<Timing>
    private lateinit var Appoint: Appointment

    // private lateinit var ListeTimeselected: ArrayList<String>
    private lateinit var listeappoint: ArrayList<Appointment>
    lateinit var doctor: Doctors
    lateinit var ListeTiming: ArrayList<Timing>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookAppointmentBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.appBar.toolbarTitle.text = "Book Appointment"

        binding.appBar.back.setOnClickListener {
            finish()
        }
        binding.appBar.rightIcon?.visibility = View.INVISIBLE

        setupDoctorDetails()
        // Loading the calendar for the first time
        loadTiming(doctor.id, CalendarUtils.selectedDate.toString(), true)//for demo purpose

        binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            // This method is triggered when there is any scrolling activity for the current page
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                //Log.i("onPageScrolled","Scrolling="+position)
            }

            // triggered when you select a new page
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.i("onPageSelected", "Scrolling=" + position)
                FiltringListTiming(position)

            }

            // triggered when there is
            // scroll state will be changed
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                //Log.i("onPageScrollState","Scrolling="+state)
            }
        })

        binding.nextBtn.setOnClickListener {
            if (Time.isEmpty()) {
                Toast.makeText(
                    this@BookAppointmentActivity,
                    "Please select the Time.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Appoint = Appointment(
                    "",
                    doctor.id,
                    doctor.category_id,
                    CalendarUtils.selectedDate.toString(),
                    Time
                )
                //   viewModel.insertAppoint(Appoint)
                val intent =
                    Intent(this@BookAppointmentActivity, AppointmentDetailsActivity::class.java)
                Log.i("AppointObj", "AppointObj  " + Appoint.toString())
                Log.i("doctorObj", "doctorObj  " + doctor.toString())
                intent.putExtra("doctorObj", doctor)
                intent.putExtra("AppointObj", Appoint)
                startActivity(intent)
            }
        }
    }


    private fun loadTiming(id_doc: String, date: String, isFirstTime: Boolean) {
        showLayout()
        viewModel.getTiming()// get liste Timing.
        viewModel.getListeAppoint(id_doc, date) // get liste Appointement(RDV)

        GlobalScope.launch(Dispatchers.Main) {
            if (isFirstTime) {
                setupWeekCalendar()
            } else {
                ListeFiltring.clear()
                appointmentAdapter.updateTimeSlotList(ListeFiltring)
            }
            delay(2000)
            viewModel.Appoint.observe(this@BookAppointmentActivity, Observer { list ->
                listeappoint = list

            })
            viewModel.TimeList.observe(this@BookAppointmentActivity, Observer { list ->
                Log.i("timeSlotList", "=$list")
                ListeTiming = list
                if (list.size > 0) {
                    hideLayout()
                }

            })

            setupPageViewer()

        }

    }

    private fun setupPageViewer() {

        var listeString: ArrayList<String> = ArrayList()
        ListeFiltring = ListeTiming.filter { s ->
            s.timeSlot == "M"
        } as ArrayList<Timing>

        for (i in 0..listeappoint.size - 1) {
            listeString.add(listeappoint.get(i).time)
        }


        appointmentAdapter =
            AppointmentAdapter(ListeFiltring, listeString, this@BookAppointmentActivity, this)
        viewPager2Adapter =
            ViewPager2Adapter(this@BookAppointmentActivity, appointmentAdapter, this)

        binding.viewpager.apply {
            adapter = viewPager2Adapter
        }
    }

    private fun setupDoctorDetails() {
        doctor = intent.extras!!.getParcelable<Doctors>("doctorObj")!!
        binding.calendarView.doctorDetails.apply {
            Glide.with(this@BookAppointmentActivity)
                .load(doctor.image)
                .into(categoryImage)
            doctorName.text = doctor.name
            categoryName.text = doctor.category
            distance.text = doctor.distance
            address.text = doctor.addres
            doctorRating.rating = doctor.Rating.toFloat()
        }


    }

    private fun setupWeekCalendar() {
        Log.i("selectedDate", "selectedDate=" + LocalDate.now())
        binding.calendarView.monthYearTV.text =
            CalendarUtils.monthYearFromDate(CalendarUtils.selectedDate)
        val days: ArrayList<LocalDate?> = CalendarUtils.daysInWeekArray(CalendarUtils.selectedDate)


        calendarAdapter = CalendarAdapter(days, this)

        binding.calendarView.calendarRecyclerView.apply {
            layoutManager = GridLayoutManager(this@BookAppointmentActivity, 7)
            hasFixedSize()
            adapter = calendarAdapter
        }

        binding.calendarView.previousWeek.setOnClickListener {
            val oldDate = CalendarUtils.selectedDate.minusWeeks(1)
            if (oldDate < LocalDate.now()) {
                Toast.makeText(
                    this@BookAppointmentActivity,
                    "You can not select Previous Week.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                CalendarUtils.selectedDate = oldDate
                setupWeekCalendar()
            }


        }
        binding.calendarView.nextWeek.setOnClickListener {

            CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1)
            setupWeekCalendar()
        }
        binding.calendarView.calenderIcon.setOnClickListener {
            val isVisibile = binding.calendarView.calendarMainView.visibility
            Log.i("isVisibile", "isVisibile=" + isVisibile)
            if (isVisibile == View.VISIBLE) {
                binding.calendarView.calendarMainView.visibility = View.GONE
            } else {
                binding.calendarView.calendarMainView.visibility = View.VISIBLE
            }
        }
    }

    override fun onCalendarItemClicked(localDate: LocalDate) {
        if (localDate < LocalDate.now()) {
            Toast.makeText(
                this@BookAppointmentActivity,
                "You can not Select Older dates.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            CalendarUtils.selectedDate = localDate

            setupWeekCalendar()
            loadTiming(doctor.id, CalendarUtils.selectedDate.toString(), false)
        }
    }

    override fun onTimeSlotClicked(time: String) {
        Time = time
       // FiltringListTiming(binding.viewpager.currentItem)

    }

    private fun FiltringListTiming(currentPos: Int) {
        ListeFiltring.clear()
        when (currentPos) {
            0 -> {
                ListeFiltring = ListeTiming.filter { s -> s.timeSlot == "M" } as ArrayList<Timing>
                true
            }
            1 -> {
                ListeFiltring = ListeTiming.filter { s -> s.timeSlot == "A" } as ArrayList<Timing>
                true
            }
            2 -> {
                ListeFiltring = ListeTiming.filter { s -> s.timeSlot == "E" } as ArrayList<Timing>
                true
            }

        }
        Log.i("morningList=" + ListeFiltring.size, "morningList=" + ListeFiltring.toString())
        appointmentAdapter.updateTimeSlotList(ListeFiltring)
    }

    override fun nextPage() {
        binding.viewpager.currentItem = binding.viewpager.currentItem + 1
        val currentItem = binding.viewpager.currentItem
        Log.i("currentItem123", "currentItem=" + currentItem)
        FiltringListTiming(currentItem)

    }

    override fun previousPage() {
        binding.viewpager.currentItem = binding.viewpager.currentItem - 1
        val currentItem = binding.viewpager.currentItem
        Log.i("currentItem", "currentItem=" + currentItem)
        FiltringListTiming(currentItem)
    }

    private fun hideLayout() {
        binding.animationViewCartPage.apply {
            visibility = View.GONE
            pauseAnimation()
        }
        binding.viewpager.apply {
            visibility = View.VISIBLE
        }

    }

    private fun showLayout() {
        binding.animationViewCartPage.apply {
            visibility = View.VISIBLE
            playAnimation()
        }
        binding.viewpager.apply {
            visibility = View.GONE
        }


    }

}