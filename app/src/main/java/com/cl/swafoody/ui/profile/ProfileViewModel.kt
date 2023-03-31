package com.cl.swafoody.ui.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cl.swafoody.data.source.local.entity.ProfileEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProfileViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _currentUser = MutableLiveData<FirebaseUser?>()

    val currentUser: LiveData<FirebaseUser?>
        get() = _currentUser

    init {
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        _currentUser.value = firebaseAuth.currentUser
    }

    fun updateProfilePhotoNew(imageUri: Uri){
        // Initialize Firebase Firestore and Storage Reference path

        val db = FirebaseFirestore.getInstance()
        val storageRef = FirebaseStorage.getInstance().reference.child("path/to/file")
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        // Upload the image to Firebase Storage
        val imageRef = storageRef.child("${currentUser?.uid}.png")
        val uploadTask = imageRef.putFile(imageUri)

        uploadTask.addOnSuccessListener {
            // Get the URL of the uploaded image
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                // Create a new user document in Firestore
                val user = ProfileEntity(
                    email = firebaseAuth.currentUser!!.email ?: "",
                    name = firebaseAuth.currentUser!!.displayName ?: "",
                    password = null,
                    photoUrl = uri.toString(),
                    uid = firebaseAuth.currentUser!!.uid
                )

                db.collection("users")
                    .document(currentUser?.uid.toString())
                    .set(user)
                    .addOnSuccessListener {
                        // Update the user's profile photo
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setPhotoUri(uri)
                            .build()

                        currentUser?.updateProfile(profileUpdates)
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("TAG", "User profile updated.")
                                }
                            }
                    }
                    .addOnFailureListener {
                        // handle failed image upload
                        Log.d("TAG", "Failed to upload image to Firebase Storage")
                    }
            }
        }
            .addOnFailureListener {
                // handle failed image upload
                Log.d("TAG", "Failed to upload image to Firebase Storage")
            }
    }
    }


//    private val _profileState = MutableLiveData< ResultState<ProfileResponseItem>>()
//    val profileState : LiveData<ResultState<ProfileResponseItem>> = _profileState
//
//    fun profile() {
//        _profileState.value = ResultState.Loading
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val user = FirebaseAuth.getInstance().currentUser
//                val data = user?.displayName
//                val email = user?.email
//                val photo_url = user?.photoUrl.toString()
//                val profile = ProfileResponseItem(data, email, photo_url)
//                _profileState.postValue(ResultState.Success(profile))
//            } catch (e: Exception) {
//                _profileState.postValue(ResultState.Failure(e))
//            }
//        }
//    }
