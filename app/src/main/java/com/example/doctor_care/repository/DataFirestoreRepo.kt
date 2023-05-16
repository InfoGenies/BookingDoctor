package com.example.doctor_care.repository

import android.content.Context
import android.util.Log
import com.example.doctor_care.models.*
import com.example.doctor_care.utils.Constante
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DataFirestoreRepo @Inject constructor(
    private val firebaseDB: FirebaseFirestore,
    private val context: Context

) {
    suspend fun getPubFirestore(): ArrayList<Pub> {
        var list: ArrayList<Pub> = ArrayList()
        try {
            list = firebaseDB.collection(Constante.Pub_TABEL).get()
                .await().documents.mapNotNull { snapShot ->
                    snapShot.toObject(Pub::class.java)
                } as ArrayList<Pub>
        } catch (e: Exception) {
            Log.d("TAG", e.message!!)
        }
        println("la taille de la liste est ${list.size}")
        return list
    }

    suspend fun getCatFirestore(): ArrayList<Category> {
        var listCat: ArrayList<Category> = ArrayList()
        try {
            listCat = firebaseDB.collection(Constante.Category_TABEL).get()
                .await().documents.mapNotNull { snapShot ->
                    snapShot.toObject(Category::class.java)
                } as ArrayList<Category>
        } catch (e: Exception) {
            Log.d("TAG", e.message!!)
        }
        println("la taille de la liste Gategorie ${listCat.size}")
        return listCat
    }

    suspend fun getDoctorsFirestore(name: String): ArrayList<Doctors> {
        var listDoc: ArrayList<Doctors> = ArrayList()
        try {
            var  listDoc =
                firebaseDB.collection(Constante.Doctor_TABEL).whereEqualTo("category", name)
                    .get()
                    .await().documents.mapNotNull { snapShot ->
                        snapShot.toObject(Doctors::class.java)
                    } as ArrayList<Doctors>
        } catch (e: Exception) {
            Log.d("TAG", e.message!!)
        }
        println("la taille de la liste Doctors ${listDoc.size}")
        return listDoc
    }

    suspend fun getAllDoctors(): ArrayList<Doctors> {
        var listDoc: ArrayList<Doctors> = ArrayList()
        try {
            listDoc =
                firebaseDB.collection(Constante.Doctor_TABEL)
                    .get()
                    .await().documents.mapNotNull { snapShot ->
                        snapShot.toObject(Doctors::class.java)
                    } as ArrayList<Doctors>
        } catch (e: Exception) {
            Log.d("TAG", e.message!!)
        }
        println("la taille de la liste Doctors ${listDoc.size}")
        return listDoc
    }

    suspend fun getNotificationFirestore(id: String): ArrayList<Notification> {
        var listNotification: ArrayList<Notification> = ArrayList()
        try {
            listNotification =
                firebaseDB.collection(Constante.Notification_TABEL).whereEqualTo("id_patient", id)
                    .get()
                    .await().documents.mapNotNull { snapShot ->
                        snapShot.toObject(Notification::class.java)
                    } as ArrayList<Notification>
        } catch (e: Exception) {
            Log.d("TAG", e.message!!)
        }

        return listNotification
    }

    suspend fun getAppoitmentFirestore(id: String, date: String): ArrayList<Appointment> {
        var listDoc: ArrayList<Appointment> = ArrayList()
        try {

            var query =
                firebaseDB.collection(Constante.Appointment_TABEL).whereEqualTo("date", date)
            listDoc = query.whereEqualTo("doctorId", id).get()
                .await().documents.mapNotNull { snapShot ->
                    snapShot.toObject(Appointment::class.java)
                } as ArrayList<Appointment>
        } catch (e: Exception) {
            Log.d("TAG", e.message!!)
        }
        println("la taille de la liste Doctors ${listDoc.size}")
        return listDoc
    }

    suspend fun getTimeSlots(): ArrayList<Timing> {
        var timeSlotList = ArrayList<Timing>()
        try {
            val collection = firebaseDB.collection("TimeSlots")

            timeSlotList = collection
                .orderBy("order", Query.Direction.ASCENDING)
                .get()
                .await().documents.mapNotNull { snapShot ->
                    snapShot.toObject(Timing::class.java)
                } as ArrayList<Timing>
        } catch (e: Exception) {
            Log.d("TAG", e.message!!)
        }
        return timeSlotList
    }

    fun insertAppointementData() {
        val newProdRef: DocumentReference = firebaseDB
            .collection("Appointments")
            .document()
        val id = newProdRef.id.toString()
        firebaseDB.collection("Appointments")
            .add(
                Appointment(
                    id,
                    "9UHbILOPtoF16cdtNN1E",
                    "Iwm9Acjf5y7H5pSU935M",
                    "07/09/2022",
                    "17:30"
                )
            ).addOnSuccessListener {
                Log.i("Successful", "Addition of Record")
            }
            .addOnFailureListener {
                Log.i("Error", "Adding Record")
            }

    }

    fun insertDoctorData() {
//// Morning
        firebaseDB.collection("TimeSlots")
            .add(Timing("M", "8:30", 1)).addOnSuccessListener {
                Log.i("Successful", "Addition of Record")
            }
            .addOnFailureListener {
                Log.i("Error", "Adding Record")
            }
        firebaseDB.collection("TimeSlots")
            .add(Timing("M", "9:00", 2)).addOnSuccessListener {
                Log.i("Successful", "Addition of Record")
            }
            .addOnFailureListener {
                Log.i("Error", "Adding Record")
            }
        firebaseDB.collection("TimeSlots")
            .add(Timing("M", "9:30", 3)).addOnSuccessListener {
                Log.i("Successful", "Addition of Record")
            }
            .addOnFailureListener {
                Log.i("Error", "Adding Record")
            }
        firebaseDB.collection("TimeSlots")
            .add(Timing("M", "10:00", 4)).addOnSuccessListener {
                Log.i("Successful", "Addition of Record")
            }
            .addOnFailureListener {
                Log.i("Error", "Adding Record")
            }
        firebaseDB.collection("TimeSlots")
            .add(Timing("M", "10:30", 5)).addOnSuccessListener {
                Log.i("Successful", "Addition of Record")
            }
            .addOnFailureListener {
                Log.i("Error", "Adding Record")
            }
        firebaseDB.collection("TimeSlots")
            .add(Timing("M", "11:00", 6)).addOnSuccessListener {
                Log.i("Successful", "Addition of Record")
            }
            .addOnFailureListener {
                Log.i("Error", "Adding Record")
            }
        firebaseDB.collection("TimeSlots")
            .add(Timing("M", "11:30", 7)).addOnSuccessListener {
                Log.i("Successful", "Addition of Record")
            }
            .addOnFailureListener {
                Log.i("Error", "Adding Record")
            }
        firebaseDB.collection("TimeSlots")
            .add(Timing("M", "12:00", 8)).addOnSuccessListener {
                Log.i("Successful", "Addition of Record")
            }
            .addOnFailureListener {
                Log.i("Error", "Adding Record")
            }
        firebaseDB.collection("TimeSlots")
            .add(Timing("M", "12:30", 9)).addOnSuccessListener {
                Log.i("Successful", "Addition of Record")
            }
            .addOnFailureListener {
                Log.i("Error", "Adding Record")
            }

//////////////////////////////////////////////////////////////////////////////////////////////

        firebaseDB.collection("TimeSlots")
            .add(Timing("A", "13:00", 10)).addOnSuccessListener {
                Log.i("Successful", "Addition of Record")
            }
            .addOnFailureListener {
                Log.i("Error", "Adding Record")
            }
        firebaseDB.collection("TimeSlots")
            .add(Timing("A", "13:30", 11)).addOnSuccessListener {
                Log.i("Successful", "Addition of Record")
            }
            .addOnFailureListener {
                Log.i("Error", "Adding Record")
            }
        firebaseDB.collection("TimeSlots")
            .add(Timing("A", "14:00", 12)).addOnSuccessListener {
                Log.i("Successful", "Addition of Record")
            }
            .addOnFailureListener {
                Log.i("Error", "Adding Record")
            }
        firebaseDB.collection("TimeSlots")
            .add(Timing("A", "14:30", 13)).addOnSuccessListener {
                Log.i("Successful", "Addition of Record")
            }
            .addOnFailureListener {
                Log.i("Error", "Adding Record")
            }
        firebaseDB.collection("TimeSlots")
            .add(Timing("A", "15:00", 14)).addOnSuccessListener {
                Log.i("Successful", "Addition of Record")
            }
            .addOnFailureListener {
                Log.i("Error", "Adding Record")
            }
        firebaseDB.collection("TimeSlots")
            .add(Timing("A", "15:30", 15)).addOnSuccessListener {
                Log.i("Successful", "Addition of Record")
            }
            .addOnFailureListener {
                Log.i("Error", "Adding Record")
            }
        firebaseDB.collection("TimeSlots")
            .add(Timing("A", "16:00", 16)).addOnSuccessListener {
                Log.i("Successful", "Addition of Record")
            }
            .addOnFailureListener {
                Log.i("Error", "Adding Record")
            }
        firebaseDB.collection("TimeSlots")
            .add(Timing("A", "16:30", 17)).addOnSuccessListener {
                Log.i("Successful", "Addition of Record")
            }
            .addOnFailureListener {
                Log.i("Error", "Adding Record")
            }


//////////////////////////////////////////////////////////////////////////////
        firebaseDB.collection("TimeSlots")
            .add(Timing("E", "17:00", 18)).addOnSuccessListener {
                Log.i("Successful", "Addition of Record")
            }
            .addOnFailureListener {
                Log.i("Error", "Adding Record")
            }
        firebaseDB.collection("TimeSlots")
            .add(Timing("E", "17:30", 19)).addOnSuccessListener {
                Log.i("Successful", "Addition of Record")
            }
            .addOnFailureListener {
                Log.i("Error", "Adding Record")
            }
        firebaseDB.collection("TimeSlots")
            .add(Timing("E", "18:00", 20)).addOnSuccessListener {
                Log.i("Successful", "Addition of Record")
            }
            .addOnFailureListener {
                Log.i("Error", "Adding Record")
            }
        firebaseDB.collection("TimeSlots")
            .add(Timing("E", "18:30", 21)).addOnSuccessListener {
                Log.i("Successful", "Addition of Record")
            }
            .addOnFailureListener {
                Log.i("Error", "Adding Record")
            }
        firebaseDB.collection("TimeSlots")
            .add(Timing("E", "19:00", 22)).addOnSuccessListener {
                Log.i("Successful", "Addition of Record")
            }
            .addOnFailureListener {
                Log.i("Error", "Adding Record")
            }
        firebaseDB.collection("TimeSlots")
            .add(Timing("E", "19:30", 23)).addOnSuccessListener {
                Log.i("Successful", "Addition of Record")
            }
            .addOnFailureListener {
                Log.i("Error", "Adding Record")
            }
        firebaseDB.collection("TimeSlots")
            .add(Timing("E", "20:00", 24)).addOnSuccessListener {
                Log.i("Successful", "Addition of Record")
            }
            .addOnFailureListener {
                Log.i("Error", "Adding Record")
            }


    }

    suspend fun insertAppointment(Appoint: Appointment) {
        val newProdRef: DocumentReference = firebaseDB
            .collection("Appointments")
            .document()
        val id = newProdRef.id.toString()
        Appoint.id = id
        firebaseDB.collection("Appointments")
            .add(Appoint).addOnSuccessListener {
                Log.i("Successful", "Addition of Record")
            }
            .addOnFailureListener {
                Log.i("Error", "Adding Record")
            }

    }

    fun insertNotification(notification: Notification) {
        val newProdRef: DocumentReference = firebaseDB
            .collection(Constante.Notification_TABEL)
            .document()
        val id = newProdRef.id.toString()
       notification.id = id
        firebaseDB.collection(Constante.Notification_TABEL)
            .add(notification).addOnSuccessListener {
                Log.i("Successful", "Addition of Record")
            }
            .addOnFailureListener {
                Log.i("Error", "Adding Record")
            }

    }

}