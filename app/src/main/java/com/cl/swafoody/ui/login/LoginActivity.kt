package com.cl.swafoody.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.cl.swafoody.R
import com.cl.swafoody.data.source.remote.ResultState
import com.cl.swafoody.databinding.ActivityLoginBinding
import com.cl.swafoody.ui.home.MainActivity
import com.cl.swafoody.ui.recipe.RecipeViewModel
import com.cl.swafoody.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var siEmail: String
    private lateinit var siPassword: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        initObservable()
        signin()
        intentToSignUp()

    }

    private fun initObservable() {
        viewModel.loading.observe(this) {
            if (!it) {
                binding.progressBar.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.VISIBLE
            }
        }

        viewModel.signInStatus.observe(this) { success ->
            if (success == true) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Email atau password salah", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun signin() {
        binding.apply {
            btnLogin.setOnClickListener {
                if (btnLogin.text != getString(R.string.please_wait)) {
                    siEmail = editTextEmail.text.toString()
                    siPassword = editTextPassword.text.toString()

                    // create validation is empty
                    editTextEmail.error = if (siEmail.isEmpty()) "Email is required" else null
                    editTextPassword.error = if (siPassword.isEmpty()) "Password is required" else null

                    viewModel.logIn(siEmail, siPassword)

                }
            }
        }
    }

    private fun intentToSignUp() {
        binding.apply {
            tvRegister.setOnClickListener {
                val intentSignUp = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intentSignUp)
            }
        }
    }

    private fun pushLogin() {
        val intentSignIn = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intentSignIn)
    }
}