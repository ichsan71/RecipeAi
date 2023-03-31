package com.cl.swafoody.ui.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.cl.swafoody.R
import com.cl.swafoody.databinding.ActivityChangePasswordBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivityChangePasswordBinding
private lateinit var viewModel: ChangePasswordViewModel
private lateinit var name: String
private lateinit var oldPassword: String
private lateinit var newPassword: String
private lateinit var confirmPassword: String

private const val pickImage = 100
private var imageUri: Uri? = null


private fun init() {
    name = binding.etName.setText(viewModel.currentUser.value?.displayName).toString()
    oldPassword = binding.etOld.text.toString()
    newPassword = binding.etNew.text.toString()
    confirmPassword = binding.etConfirm.text.toString()
}

class ChangePassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel = ViewModelProvider(this)[ChangePasswordViewModel::class.java]

        init()
        observer()
        updatePhoto()

        binding.btnChange.setOnClickListener {
            val oldPassword = binding.etOld.text.toString()
            val newPassword = binding.etNew.text.toString()
            val name = binding.etName.text.toString()
            val confirmPassword = binding.etConfirm.text.toString()
            Toast.makeText(this, oldPassword, Toast.LENGTH_SHORT).show()
            viewModel.updatePassword(newPassword, oldPassword, name)

            // move to fragment profile
            val profileFragment = ProfileFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.navigation_profile, profileFragment, "my_fragment_tag")
            transaction.addToBackStack("my_fragment_tag")
            transaction.commit()
//            MaterialAlertDialogBuilder(this)
//                .setTitle("are you sure to make changes?")
//                .setNeutralButton("Cancel") { _, _ ->
//                    // Respond to neutral button press
//                    Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
//                }
//                .setPositiveButton("Oke") { _, _ ->
//                    // Respond to positive button press
//                    observer()
//                    binding.etName.setText(viewModel.currentUser.value?.displayName)
//                    // check password and confirm password
//                    if (!checkPassword(newPassword, confirmPassword)) {
//                        return@setPositiveButton
//                    } else {
//                        //viewModel.updateProfile(newPassword, oldPassword, name)
//                        viewModel.updatePassword(newPassword, oldPassword)
//                    }
//                }
//                .show()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            val oldPassword = binding.etOld.text.toString()
            val newPassword = binding.etNew.text.toString()
            val name = binding.etName.text.toString()
            imageUri = data?.data
            imageUri?.let { it1 -> viewModel.updateProfile(name, oldPassword, newPassword, it1) }
        }
    }

    // update photo_url from gallery and viewmodel
    @SuppressLint("IntentReset")
    private fun updatePhoto() {
        binding.apply {
            imgPhoto.setOnClickListener {
                // get photo from gallery
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(intent, pickImage)
            }
        }
    }

    private fun observer() {
        viewModel.apply {
            currentUser.observe(this@ChangePassword, Observer {
                // instance profile fragment
                updateUI(it)
            })
        }
    }

    // create function to check password and confirm password
    private fun checkPassword(newPassword: String, confirmPassword: String): Boolean {
        if (newPassword.isEmpty()) {
            binding.etNew.error = "Password is required"
            binding.etNew.requestFocus()
            return false
        } else if (newPassword.length < 6) {
            binding.etNew.error = "Password must be at least 6 characters"
            binding.etNew.requestFocus()
            return false
        } else if (confirmPassword.isEmpty()) {
            binding.etConfirm.error = "Confirm password is required"
            binding.etConfirm.requestFocus()
            return false
        }
        return if (newPassword != confirmPassword) {
            binding.etConfirm.error = "Password not match"
            binding.etConfirm.requestFocus()
            false
        } else {
            binding.etConfirm.error = null
            true
        }
    }

    fun updateUI(user: FirebaseUser?) {
        val loading = binding.progressBar

        loading.visibility = ProgressBar.VISIBLE
        lifecycleScope.launch(Dispatchers.IO){
            if (user != null) {
                val storage = FirebaseStorage.getInstance()
                val storageRef = storage.reference

                // Create a reference to the file you want to download
                val photoRef = storageRef.child("path/to/file/${user.uid}.png")

                withContext(Dispatchers.Main){
                    // check if file exists
                    photoRef.metadata.addOnSuccessListener {
                        // File exists
                        Log.d(ContentValues.TAG, "File exists")
                        // Download the photo data as a byte array
                        photoRef.getBytes(1024 * 1024)
                            .addOnSuccessListener { data ->
                                // Photo data downloaded successfully
                                // Do something with the data, such as displaying it in an ImageView
                                val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                                val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap)
                                roundedBitmapDrawable.isCircular = true
                                binding.imgPhoto.setImageDrawable(roundedBitmapDrawable)
                                loading.visibility = ProgressBar.GONE
                            }
                            .addOnFailureListener { exception ->
                                // Photo data download failed
                                // Handle the error
                                Log.e(ContentValues.TAG, "Error downloading photo: $exception")
                                binding.imgPhoto.setImageResource(R.mipmap.femaleuser)
                                loading.visibility = ProgressBar.GONE
                            }
                    }.addOnFailureListener {
                        // File does not exist
                        Log.d(ContentValues.TAG, "File does not exist")
                        binding.imgPhoto.setImageResource(R.mipmap.femaleuser)
                        loading.visibility = ProgressBar.GONE
                    }
                }

            } else {
                binding.imgPhoto.setImageResource(R.mipmap.femaleuser)
                Log.d(ContentValues.TAG, "User is null")
                loading.visibility = ProgressBar.GONE
            }
        }

    }

}









