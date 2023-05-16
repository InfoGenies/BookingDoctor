package com.example.doctor_care.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.doctor_care.models.UserInfoModel
import com.example.doctor_care.R
import com.example.doctor_care.utils.Constante.Companion.LOADING_ANNOTATION
import com.example.doctor_care.utils.extention.showToast
import com.example.doctor_care.utils.helper.Resource
import com.example.doctor_care.viewModel.UserInfoViewModel
import com.example.doctor_care.views.Authentification
import com.example.doctor_care.databinding.FragmentProfilBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class ProfilFragment : Fragment() {
    private val userInfoViewModel by activityViewModels<UserInfoViewModel>()
    private lateinit var binding: FragmentProfilBinding
    private var userInfoModel: UserInfoModel? = null

    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog: Dialog

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilBinding.inflate(inflater, container, false);
        binding.LogOut.setOnClickListener {
            logout()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeListener()

    }

    private fun observeListener() {
        userInfoViewModel.getUserInformation()
        userInfoViewModel.userInformationLiveData.observe(viewLifecycleOwner, Observer { userInfo ->
            when (userInfo) {
                is Resource.Success -> {
                    userInfoModel = userInfo.data
                    Initiviews(userInfoModel)
                    loadingDialog.hide()
                }
                is Resource.Error -> {
                    loadingDialog.hide()
                    showToast(userInfo.msg!!)
                }
                is Resource.Loading -> loadingDialog.show()
            }
        })

    }

    private fun Initiviews(model: UserInfoModel?) {
        binding.apply {
            Glide.with(requireContext()).load(model!!.userImage).into(profileImage)
            nameId.text = model.userName
            userName.text = model.userName
            addressId.text = model.userLocationName
        }
    }
    fun logout() {
        firebaseAuth.signOut()
        navigateToAuthFragment()
        showToast(getString(R.string.logOutMessage))
    }

    private fun navigateToAuthFragment() {
        val intent = Intent(requireActivity(), Authentification::class.java)
        startActivity(intent)
    }

}