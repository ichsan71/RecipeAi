package com.cl.swafoody.ui.profile

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.recreate
import androidx.lifecycle.*
import com.cl.swafoody.data.source.local.entity.ProfileEntity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ChangePasswordViewModel: ViewModel() {

    // create a function to update password from firebase auth

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

    fun updateProfile(newPassword:String, oldPassword: String, name: String, imageUri: Uri) {
        val user = firebaseAuth.currentUser
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
                ProfileEntity(
                    email = firebaseAuth.currentUser!!.email ?: "",
                    name = name,
                    password = null,
                    photoUrl = uri.toString(),
                    uid = firebaseAuth.currentUser!!.uid
                )

                db.collection("users")
                    .document(currentUser?.uid.toString())
                    .update("name", name, "photoUrl", uri.toString())
                    .addOnSuccessListener {
                        // Update the user's profile photo
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setPhotoUri(uri)
                            .setDisplayName(name)
                            .build()

                        currentUser?.updateProfile(profileUpdates)
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("Profile", "User profile updated.")
                                }
                            }
                    }
                // recreate activity profile
                //recreate()
            }
        }
            .addOnFailureListener {
                // handle failed image upload
                Log.d("TAG", "Failed to upload image to Firebase Storage")
            }

        updatePassword(newPassword, oldPassword, name)
    }

    fun updatePassword(newPassword: String, oldPassword: String, name: String) {
        val user = FirebaseAuth.getInstance().currentUser
        // update name
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()

        user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Profile", "User profile updated.")
            } else {
                Log.d("Profile", "User profile update failed.")
            }
        }

        val credential = EmailAuthProvider
            .getCredential(user?.email.toString(), oldPassword)

        user?.reauthenticate(credential)
            ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("Profile", "User re-authenticated.")
                    user.updatePassword(newPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("Profile", "User password updated.")
                            } else {
                                Log.d("Profile", "User password update failed.")
                            }
                        }
                } else {
                    Log.d("Profile", "User re-authentication failed.${it.exception}")
                }
            }
    }
}