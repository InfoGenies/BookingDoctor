package com.example.doctor_care.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doctor_care.adapters.SearchAdapter
import com.example.doctor_care.models.Doctors
import com.example.doctor_care.viewModel.DatafirestoreViewModel
import com.example.doctor_care.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.appcompat.widget.SearchView
import com.example.doctor_care.views.BookAppointmentActivity

@AndroidEntryPoint
class SearchFragment : Fragment(), SearchAdapter.IDoctorListener {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: DatafirestoreViewModel by activityViewModels<DatafirestoreViewModel>()
    lateinit var adapters: SearchAdapter
    lateinit var ListeDoctors: ArrayList<Doctors>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ListeDoctors = ArrayList()
        adapters = SearchAdapter(requireContext(), this@SearchFragment)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        hideLayout()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeListener()

    }

    private fun observeListener() {
        viewModel.getAllDoctors()
        GlobalScope.launch(Dispatchers.Main) {
            delay(800) // Waiting
            if ( view != null){
                viewModel.AllDoctorrsList.observe(viewLifecycleOwner, Observer { list ->
                    ListeDoctors = list
                })
                if (ListeDoctors.size > 0) {
                    showLayout()
                    stupeAdapter(ListeDoctors)
                }
                binding.countrySearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        adapters.filter.filter(newText)
                        return false
                    }

                })
            }
        }
    }

    fun stupeAdapter(doctlist: ArrayList<Doctors>) {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            hasFixedSize()
            adapters.Update(doctlist)
            adapter = adapters
        }
    }

    private fun hideLayout() {
        binding.animationViewCartPage.apply {
            visibility = View.VISIBLE
            playAnimation()

        }
        binding.recyclerView.visibility = View.GONE
    }

    private fun showLayout() {
        binding.animationViewCartPage.apply {
            visibility = View.GONE
            pauseAnimation()

        }
        binding.recyclerView.visibility = View.VISIBLE
    }

    override fun onDoctorClick(doctor: Doctors) {
        val intent = Intent(requireContext(), BookAppointmentActivity::class.java)
        intent.putExtra("doctorObj", doctor)
        startActivity(intent)
    }


}