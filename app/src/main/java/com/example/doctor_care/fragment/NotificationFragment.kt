package com.example.doctor_care.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.example.doctor_care.adapters.NotificationAdapter
import com.example.doctor_care.models.Notification
import com.example.doctor_care.viewModel.DatafirestoreViewModel
import com.example.doctor_care.databinding.FragmentNotificationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationFragment : Fragment() {
    private lateinit var binding: FragmentNotificationBinding
    private val viewModel: DatafirestoreViewModel by activityViewModels<DatafirestoreViewModel>()
    lateinit var notificationAdpter: NotificationAdapter
    lateinit var animationView: LottieAnimationView
    lateinit var notificationList: ArrayList<Notification>





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        hideLayout()


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeListener()
        }

    private fun observeListener() {
        viewModel.getNotification("1xzw")
        GlobalScope.launch(Dispatchers.Main) {
            delay(1500)
            if ( view != null){
                viewModel.Notifliste.observe(viewLifecycleOwner, Observer { list ->
                    // Log.i("doctors=", "doctors=${list.size}")
                    notificationList = list
                    if (list.size>0)
                        showLayout()
                })
                setupRV()
            }

        }
    }


    private fun setupRV() {
        notificationAdpter = NotificationAdapter(notificationList, requireActivity())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            hasFixedSize()
            adapter = notificationAdpter
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
}