package com.example.doctor_care.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.TranslateAnimation
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doctor_care.adapters.DrawerMenuAdapter
import com.example.doctor_care.adapters.IDrawerMenuAdapter
import com.example.doctor_care.R
import com.example.doctor_care.databinding.ActivityMainBinding
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), IDrawerMenuAdapter {
    private lateinit var binding: ActivityMainBinding
    private var drawerOpened: Boolean = false
    lateinit var drawerMenuAdapter: DrawerMenuAdapter
    lateinit var navController: NavController
    lateinit var badge_notifiction: BadgeDrawable
    lateinit var badge_note: BadgeDrawable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
        title = ""
        actionBar?.elevation = 0f
        setSupportActionBar(binding.mainAppBar.toolbar)
        binding.mainAppBar.appbarLayout.outlineProvider = null
        binding.mainAppBar.toolbarTitle.text = "Constantine"
        drawerMenuAdapter = DrawerMenuAdapter(this)
        binding.menuRV.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            hasFixedSize()
            adapter = drawerMenuAdapter
        }

        binding.mainAppBar.home.setOnClickListener {
            toggleDrawerNavigation()
        }
        binding.mainAppBar.homeSearch.setOnClickListener {
            navController.navigate(R.id.searchFragment)
        }

        binding.drawerInnerLayout.backBtn.setOnClickListener {
            toggleDrawerNavigation()
        }

    }

    private fun setupNavigation() {

        val navView: BottomNavigationView = binding.bottomNavigation
        badge_notifiction = binding.bottomNavigation.getOrCreateBadge(R.id.notificationFragment2)
        badge_notifiction.backgroundColor = getColor(R.color.basicBlue)
        badge_notifiction.maxCharacterCount = 5
        badge_notifiction.number = 100
        badge_notifiction.setVisible(true)

        navController = findNavController(R.id.fragmentContainerView)

        navView.setupWithNavController(navController)
    }

    private fun toggleDrawerNavigation() {
        if (!drawerOpened) {
            binding.drawerLayout.visibility = View.VISIBLE
            binding.menuRV.visibility = View.VISIBLE
            val animate = TranslateAnimation(
                -binding.drawerLayout.width.toFloat(),
                0F,
                0f,
                0F
            )
            animate.duration = 500
            animate.fillAfter = true
            binding.drawerLayout.startAnimation(animate)

        } else {
            binding.drawerLayout.visibility = View.GONE
            val animate = TranslateAnimation(
                0F,
                -binding.drawerLayout.width.toFloat(),
                0F,
                0F
            )
            animate.duration = 500
            animate.fillAfter = true
            binding.drawerLayout.startAnimation(animate)
            binding.menuRV.visibility = View.GONE

        }
        //binding.drawerLayout.setOnClickListener(null)
        drawerOpened = !drawerOpened
    }

    override fun onMenuItemClicked(id: Int) {
        toggleDrawerNavigation()

        when (id) {
            0 -> {
                navController.navigate(R.id.homeFragment)
                true
            }
            1 -> {
                navController.navigate(R.id.searchFragment)
                true
            }
            2 -> {

                badge_notifiction = binding.bottomNavigation.getBadge(R.id.notificationFragment2)!!
                badge_notifiction.clearNumber()
                badge_notifiction.setVisible(false)
                navController.navigate(R.id.notificationFragment2)
                true
            }
            3 -> {
                navController.navigate(R.id.profileFragment)
                true
            }
            else -> {
                navController.navigate(R.id.homeFragment)
                true
            }
        }


    }


    override fun onBackPressed() {
      finish()
    }


}