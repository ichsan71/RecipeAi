package com.cl.swafoody.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.cl.swafoody.data.source.remote.response.LoginResponse
import com.cl.swafoody.databinding.ActivityOnboardingBinding
import com.cl.swafoody.ui.home.MainActivity
import com.cl.swafoody.ui.login.LoginActivity
import com.cl.swafoody.utils.SharedPrefs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.orhanobut.hawk.Hawk

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        Handler(Looper.getMainLooper()).postDelayed({
            val currentUser : FirebaseUser? = auth.currentUser
            val intent = if (currentUser == null) {
                Intent(this, LoginActivity::class.java)
            }
            else {
                Intent(this, MainActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, TIME)
    }

    companion object {
        private const val TIME = 3000L
    }
}