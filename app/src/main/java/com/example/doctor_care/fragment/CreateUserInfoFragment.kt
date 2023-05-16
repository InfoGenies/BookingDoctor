package com.example.doctor_care.fragment

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.doctor_care.R
import com.example.doctor_care.utils.Constante.Companion.LOADING_ANNOTATION
import com.example.doctor_care.utils.extention.hideBottomNav
import com.example.doctor_care.utils.helper.Resource
import com.example.doctor_care.viewModel.PhoneAuthViewModel
import com.example.doctor_care.viewModel.UserInfoViewModel
import com.example.doctor_care.views.MainActivity
import com.example.doctor_care.databinding.FragmentCreateUserInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named
@AndroidEntryPoint
class CreateUserInfoFragment : Fragment() {
    private lateinit var binding: FragmentCreateUserInfoBinding
    private val authViewModel by activityViewModels<PhoneAuthViewModel>()
    private val userInfoViewModel by activityViewModels<UserInfoViewModel>()
    private var mImageUri: Uri? = null

    private val args by navArgs<CreateUserInfoFragmentArgs>()
    private val userInfoModel by lazy { args.userInfoModel }

    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).hideBottomNav()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateUserInfoBinding.inflate(inflater, container, false)

        binding.selectUserLocation.setOnClickListener {
            selectUserLocation()
        }
        binding.userProfileImage.setOnClickListener {
            changeUserProfileImage()
        }
        binding.userInfoSubmitButton.setOnClickListener {
            submitUserInfo()
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeListener()
    }
    override fun onResume() {
        super.onResume()
        showUserImage()
    }


    private fun observeListener() {
        userInfoViewModel.userLocationLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.apply {
                    selectLocationEditText.setText(it)
                    selectLocationEditText.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.green)
                    )
                }
            }
        })
        authViewModel.userInfoLiveData.observe(viewLifecycleOwner, Observer { info ->
            when (info) {
                is Resource.Success -> {
                    loadingDialog.hide()
                    Toast.makeText(
                        requireContext(),
                        "${info.data!!}",
                        Toast.LENGTH_SHORT
                    ).show()
                        authViewModel.setUserInformationValue()
                        closeFragment()
                }
                is Resource.Error -> {
                    loadingDialog.hide()
                    Toast.makeText(
                        requireContext(),
                        "${info.msg!!}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> loadingDialog.show()
            }
        })
    }

    fun submitUserInfo() = with(binding) {
        val userName = userNameEditText.text.toString().trim()
        val userLocation = selectLocationEditText.text.toString().trim()
        if (userName.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "${getString(R.string.addUserName)}",
                Toast.LENGTH_SHORT
            ).show()
            return@with
        }
        if (mImageUri == null && userInfoModel == null) {
            Toast.makeText(
                requireContext(),
                "${getString(R.string.addUserImage)}",
                Toast.LENGTH_SHORT
            ).show()
            return@with
        }
        if (userLocation.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "${getString(R.string.addUserLocation)}",
                Toast.LENGTH_SHORT
            ).show()
            return@with
        }
        authViewModel.uploadUserInformation(
            userName,
            mImageUri,
            userLocation
        )

    }

    fun selectUserLocation() {
        val action =
            CreateUserInfoFragmentDirections.actionCreateUserInfoFragmentToLocateUserLocationFragment2()
        findNavController().navigate(action)
    }

    fun changeUserProfileImage() {
        selectImageFromGallery()
    }

    private fun selectImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(Intent.createChooser(intent, "Select Picture"))
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            mImageUri = result.data?.data
            showUserImage()
        }

    private fun showUserImage() {
        if (mImageUri != null)
            binding.userProfileImage.setImageURI(mImageUri)
    }


    fun closeFragment() {
        findNavController().popBackStack()
    }




}
