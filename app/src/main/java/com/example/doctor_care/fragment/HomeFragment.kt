package com.example.doctor_care.fragment

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.doctor_care.R
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.airbnb.lottie.LottieAnimationView
import com.example.doctor_care.adapters.CategoryAdapter
import com.example.doctor_care.adapters.ICategoryListener
import com.example.doctor_care.adapters.ImageSlideAdapter
import com.example.doctor_care.models.Category
import com.example.doctor_care.viewModel.DatafirestoreViewModel
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.doctor_care.models.Notification
import com.example.doctor_care.utils.Constante.Companion.LOADING_ANNOTATION
import com.example.doctor_care.utils.extention.*
import com.example.doctor_care.utils.helper.Resource
import com.example.doctor_care.viewModel.UserInfoViewModel
import com.example.doctor_care.views.DoctorListActivity
import com.example.doctor_care.views.MainActivity
import com.example.doctor_care.databinding.FragmentHomeBinding
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class HomeFragment : Fragment(), ICategoryListener {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel by activityViewModels<DatafirestoreViewModel>()
    private val userInfoViewModel by activityViewModels<UserInfoViewModel>()
    private val bottomNavigationView by lazy {
        (activity as MainActivity).findViewById<BottomNavigationView>(
            R.id.bottom_navigation
        )
    }

    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog: Dialog

    lateinit var catAdapter: CategoryAdapter
    lateinit var animationView: LottieAnimationView
    lateinit var viewPagerAdapter: ImageSlideAdapter
    lateinit var indicator: WormDotsIndicator
    lateinit var viewpager: ViewPager
    lateinit var notification: Notification
    var location: Location? = null
    val images: ArrayList<String>? = ArrayList()
    private lateinit var categoryList: ArrayList<Category>
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("HomeFragment is onCreateView")

        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        //   (activity as MainActivity).showBottomNav()

        getLastLocation()

        animationView = binding.animationViewCartPage
        viewpager = binding.imageSliderItem
        categoryList = ArrayList()
        hideLayout()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("HomeFragment is onViewCreated")
        observeListener()
    }


    private fun observeListener() {
        userInfoViewModel.getUserInformation()
        GlobalScope.launch(Dispatchers.Main) {
            delay(800)
        // check if user have data into firebase firestore
        userInfoViewModel.userInformationLiveData.observe(viewLifecycleOwner, Observer { userInfo ->
            when (userInfo) {
                is Resource.Success -> {
                    viewModel.getPub()
                    viewModel.getCat()
                    initViewPager()
                }
                is Resource.Error -> {
                    loadingDialog.hide()
                    navigateToCreateUserInfoFragment()
                }
                is Resource.Loading -> {
                    println("Data is checking ")
                    loadingDialog.show()}
            }

        })

            if (view != null) {
                viewModel.PubList.observe(viewLifecycleOwner, Observer { list ->
                    if (images!!.size > 0) {
                        images.clear()
                    }

                    for (i in 0..list.size - 1) {
                        images?.add(list.get(i).imagePub)
                    }
                    viewPagerAdapter.notifyDataSetChanged()
                })
                viewModel.CatList.observe(viewLifecycleOwner, Observer { list ->
                    categoryList = list
                    setupRV()
                })

                delay(2000)

                showLayout()

            }
        }

    }

    private fun initViewPager() {
        images?.let {
            viewPagerAdapter = ImageSlideAdapter(requireContext(), it)
            viewpager.adapter = viewPagerAdapter
            indicator = binding.wormDotsIndicator
            indicator.attachTo(viewpager)
        }

    }

    private fun setupRV() {
        catAdapter = CategoryAdapter(requireContext(), categoryList, this)
        binding.categoryRV.apply {
            layoutManager = LinearLayoutManager(requireContext())
            hasFixedSize()
            adapter = catAdapter
        }
    }

    private fun showLayout() {
        animationView.visibility = View.GONE
        animationView.pauseAnimation()
        viewpager.visibility = View.VISIBLE
        binding.categoryRV.visibility = View.VISIBLE

    }

    private fun hideLayout() {
        viewpager.visibility = View.GONE
        animationView.visibility = View.VISIBLE
        animationView.playAnimation()
        binding.categoryRV.visibility = View.GONE
    }

    override fun onCategoryClick(category_id: String, category_name: String) {
        val intent = Intent(requireContext(), DoctorListActivity::class.java)
        intent.putExtra("categoryId", category_id)
        intent.putExtra("categoryName", category_name)
        intent.putExtra("latit", location!!.latitude)
        intent.putExtra("long", location!!.longitude)
        startActivity(intent)

    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isMapsEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity(), { task ->
                    location = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {


                    }
                })
            } else {
                Toast.makeText(requireContext(), "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation!!

        }
    }

    private fun showBottomNavigationView() {
        bottomNavigationView.animate().scaleY(1f)
        bottomNavigationView.show()
    }

    override fun onPause() {
        super.onPause()
        println("HomeFragment is onPause")
    }
    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }

    override fun onStop() {
        super.onStop()
        println("HomeFragment is onStop")

        if (loadingDialog.isShowing)
            loadingDialog.hide()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        println("HomeFragment is onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("HomeFragment is onDestroy")

    }


    private fun navigateToCreateUserInfoFragment() {
        val action = HomeFragmentDirections.actionHomeFragmentToCreateUserInfoFragment()
        findNavController().navigate(action)
    }



}