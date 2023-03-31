package com.cl.swafoody.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.cl.swafoody.R
import com.cl.swafoody.data.source.remote.ResultState
import com.cl.swafoody.databinding.ActivityRegisterBinding
import com.cl.swafoody.ui.home.MainActivity
import com.cl.swafoody.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    private lateinit var siName: String
    private lateinit var siEmail: String
    private lateinit var siPassword: String
    private lateinit var siConfirmPassword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        initObservable()
        signup()
        intentToSignIn()
    }

    private fun initObservable() {
        viewModel.emailError.observe(this) {
            if (it.isNotEmpty()) {
                binding.editTextConfirmPassword.error = null
            } else {
                binding.editTextConfirmPassword.error = it
                binding.editTextConfirmPassword.requestFocus()
            }
        }
        viewModel.nameError.observe(this) {
            if (it.isNotEmpty()) {
                binding.editTextConfirmPassword.error = null
            } else {
                binding.editTextConfirmPassword.error = it
                binding.editTextConfirmPassword.requestFocus()
            }
        }
        viewModel.passwordError.observe(this) {
            if (it.isNotEmpty()) {
                binding.editTextConfirmPassword.error = null
            } else {
                binding.editTextConfirmPassword.error = it
                binding.editTextConfirmPassword.requestFocus()
            }
        }
        viewModel.confirmPasswordError.observe(this) {
            if (it.isNotEmpty()) {
                binding.editTextConfirmPassword.error = null
            } else {
                binding.editTextConfirmPassword.error = it
                binding.editTextConfirmPassword.requestFocus()
            }
        }
        viewModel.registerStatus.observe(this) { success ->
            if (success == true) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                Log.d("RegisterActivity", "Authentication failed.")
            }
        }
    }

    private fun signup() {
        binding.apply {
            btnRegister.setOnClickListener {
                if (btnRegister.text != getString(R.string.please_wait)) {
                    siName = editTextName.text.toString()
                    siEmail = editTextEmail.text.toString()
                    siPassword = editTextPassword.text.toString()
                    siConfirmPassword = editTextConfirmPassword.text.toString()

                    initObservable()
                    viewModel.register(siName, siEmail, siPassword, siConfirmPassword)
                }
            }
        }
    }

    private fun intentToSignIn() {
        binding.apply {
            tvLogin.setOnClickListener {
                val intentSignUp = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intentSignUp)
            }
        }
    }

    private fun pushRegister() {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
    }
}