package com.cl.swafoody.ui.login

import androidx.lifecycle.*
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginViewModel() : ViewModel(), LifecycleObserver {
    var loading: MutableLiveData<Boolean> = MutableLiveData()

    private lateinit var auth: FirebaseAuth
    init {

        loading.postValue(false)
    }

    private val _signInStatus = MutableLiveData<Boolean>()
    val signInStatus: LiveData<Boolean> = _signInStatus


    fun logIn(email: String, password: String) {
        auth = FirebaseAuth.getInstance()
        loading.postValue(true)

        viewModelScope.launch(Dispatchers.IO){
            try {
                auth.let { login->
                    login.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task: Task<AuthResult> ->
                            if (task.isSuccessful) {
                                _signInStatus.postValue(true)
                                // Sign in success, update UI with the signed-in user's information
                            } else {
                                _signInStatus.postValue(false)
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

