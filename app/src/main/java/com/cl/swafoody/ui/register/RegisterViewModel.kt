package com.cl.swafoody.ui.register

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cl.swafoody.data.source.remote.ResultState
import com.cl.swafoody.data.source.remote.post.RegisterBody
import com.cl.swafoody.network.ApiConfig
import com.cl.swafoody.ui.home.MainActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class RegisterViewModel() : ViewModel() {

    private lateinit var auth: FirebaseAuth

    var loading: MutableLiveData<Boolean> = MutableLiveData()

    private val _nameError = MutableLiveData<String>()
    val nameError : LiveData<String> = _nameError

    private val _emailError = MutableLiveData<String>()
    val emailError : LiveData<String> = _emailError

    private val _passwordError = MutableLiveData<String>()
    val passwordError : LiveData<String> = _passwordError

    private val _confirmPasswordError = MutableLiveData<String>()
    val confirmPasswordError : LiveData<String> = _confirmPasswordError

    private val _registerStatus = MutableLiveData<Boolean>()
    val registerStatus: LiveData<Boolean> = _registerStatus

    fun register(name: String, email: String, password: String, confirmPassword: String) {
        loading.postValue(true)
        // make validation name, email, password, confirmPassword in viewmodel
        if (name.isEmpty()) {
            _nameError.postValue("Name is required")
            loading.postValue(false)
        } else if (email.isEmpty()) {
            _emailError.postValue("Email is required")
            loading.postValue(false)
        } else if (password.isEmpty()) {
            _passwordError.postValue("Password is required")
            loading.postValue(false)
        } else if (confirmPassword.isEmpty()) {
            _confirmPasswordError.postValue("Confirm Password is required")
            loading.postValue(false)
        } else if (password != confirmPassword) {
            _confirmPasswordError.postValue("Confirm Password is not match")
            loading.postValue(false)
        } else {
            pushRegister(name, email, password)
        }

    }

    fun pushRegister(name: String, email: String, password: String){
        auth = FirebaseAuth.getInstance()
        loading.postValue(true)
        viewModelScope.launch(Dispatchers.IO){
            try {
                auth.let { register->
                    register.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task: Task<AuthResult> ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                val user = auth.currentUser
                                val profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build()
                                user?.updateProfile(profileUpdates)
                                _registerStatus.postValue(true)
                            } else {
                                _registerStatus.postValue(false)
                                Log.w("RegisterViewModel", "createUserWithEmail:failure", task.exception)
                            }
                            loading.postValue(false)
                        }
                }
            } catch (e: Exception){
                loading.postValue(false)
            }
        }


    }
}