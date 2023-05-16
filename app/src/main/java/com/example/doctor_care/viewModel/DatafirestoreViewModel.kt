package com.example.doctor_care.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doctor_care.models.*
import com.example.doctor_care.repository.DataFirestoreRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DatafirestoreViewModel @Inject constructor(private val repository: DataFirestoreRepo) :
    ViewModel() {


    private var _AllDoctorsList: MutableLiveData<ArrayList<Doctors>>
    val AllDoctorrsList: LiveData<ArrayList<Doctors>> get() = _AllDoctorsList


    private var _NotificationList: MutableLiveData<ArrayList<Notification>>
    val Notifliste: LiveData<ArrayList<Notification>> get() = _NotificationList

    private var _AppointList: MutableLiveData<ArrayList<Appointment>>
    val Appoint: LiveData<ArrayList<Appointment>> get() = _AppointList

    private var _TimeList: MutableLiveData<ArrayList<Timing>>
    val TimeList: LiveData<ArrayList<Timing>> get() = _TimeList

    private var _PubList: MutableLiveData<ArrayList<Pub>>
    val PubList: LiveData<ArrayList<Pub>> get() = _PubList

    private var _CatList: MutableLiveData<ArrayList<Category>>
    val CatList: LiveData<ArrayList<Category>> get() = _CatList

    private var _DocList: MutableLiveData<ArrayList<Doctors>>
    val DocList: LiveData<ArrayList<Doctors>> get() = _DocList


    init {
        _AllDoctorsList = MutableLiveData<ArrayList<Doctors>>()
        _NotificationList = MutableLiveData<ArrayList<Notification>>()
        _AppointList = MutableLiveData<ArrayList<Appointment>>()
        _TimeList = MutableLiveData<ArrayList<Timing>>()
        _PubList = MutableLiveData<ArrayList<Pub>>()
        _CatList = MutableLiveData<ArrayList<Category>>()
        _DocList = MutableLiveData<ArrayList<Doctors>>()
    }

    fun getPub() {
        viewModelScope.launch {
            _PubList.postValue(repository.getPubFirestore())
        }
    }

    fun getCat() {
        viewModelScope.launch {
            _CatList.postValue(repository.getCatFirestore())
        }
    }

    fun getDoctors(name: String) {
        viewModelScope.launch {
            _DocList.postValue(repository.getDoctorsFirestore(name))
        }
    }
    fun getAllDoctors() {
        viewModelScope.launch {
            _AllDoctorsList.postValue(repository.getAllDoctors())
        }
    }

    fun getListeAppoint(id: String, date: String) {
        viewModelScope.launch {
            _AppointList.postValue(repository.getAppoitmentFirestore(id, date))
        }
    }

    fun getTiming() {
        viewModelScope.launch {
            _TimeList.postValue(repository.getTimeSlots())
        }
    }

    fun getNotification(id :String) {
        viewModelScope.launch {
            _NotificationList.postValue(repository.getNotificationFirestore(id))
        }
    }


    fun insertDoctorData() {
        viewModelScope.launch {
            repository.insertDoctorData()
        }
    }

    fun insertApointData() {
        viewModelScope.launch {
            repository.insertAppointementData()
        }
    }

    fun insertAppoint(Appoint: Appointment) {
        viewModelScope.launch {
            repository.insertAppointment(Appoint)
        }

    }

    fun insertNotification(notification: Notification) {
        viewModelScope.launch {
            repository.insertNotification(notification)
        }
    }


}