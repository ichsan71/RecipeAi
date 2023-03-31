package com.cl.swafoody.ui.profile

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.recreate
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.cl.swafoody.R
import com.cl.swafoody.data.source.remote.ResultState
import com.cl.swafoody.databinding.FragmentProfileBinding
import com.cl.swafoody.ui.login.LoginActivity
import com.cl.swafoody.ui.login.LoginViewModel
import com.cl.swafoody.utils.SharedPrefs.Companion.KEY_LOGIN
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: ProfileViewModel

    private val pickImage = 100
    private var imageUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        observer()
        changePassword()
        logout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observer() {
        viewModel.apply {
            currentUser.observe(viewLifecycleOwner, Observer {
                updateUI(it)
            })
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        val loading = binding?.progressBar
        loading?.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            if (user != null) {
                binding?.tvName!!.text = user.displayName
                val storage = FirebaseStorage.getInstance()
                val storageRef = storage.reference

                // Create a reference to the file you want to download
                val photoRef = storageRef.child("path/to/file/${user.uid}.png")

                // check if file exists
                photoRef.metadata.addOnSuccessListener {
                    // File exists
                    Log.d(TAG, "File exists")
                    // Download the photo data as a byte array
                    photoRef.getBytes(1024 * 1024)
                        .addOnSuccessListener { data ->
                            // Photo data downloaded successfully
                            // Do something with the data, such as displaying it in an ImageView
                            val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                            binding!!.imgPhoto.setImageBitmap(bitmap)
                            loading?.visibility = View.GONE
                        }
                        .addOnFailureListener { exception ->
                            // Photo data download failed
                            // Handle the error
                            Log.d(TAG, "Error downloading photo: $exception")
                            binding!!.imgPhoto.setImageResource(R.mipmap.femaleuser)
                            loading?.visibility = View.GONE
                        }
                }.addOnFailureListener {
                    // File does not exist
                    Log.d(TAG, "File does not exist")
                    binding!!.imgPhoto.setImageResource(R.mipmap.femaleuser)
                    loading?.visibility = View.GONE
                }
            } else {
                binding!!.imgPhoto.setImageResource(R.mipmap.femaleuser)
                Log.d(TAG, "User is null")
                loading?.visibility = View.GONE
            }
        }
        }


//    // update photo_url from gallery and viewmodel
//    @SuppressLint("IntentReset")
//    private fun updatePhoto() {
//        binding?.apply {
//            imgPhoto.setOnClickListener {
//                // get photo from gallery
//                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//                startActivityForResult(intent, pickImage)
//            }
//        }
//    }
//    @Deprecated("Deprecated in Java")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == RESULT_OK && requestCode == pickImage) {
//            imageUri = data?.data
//            imageUri?.let { it1 -> viewModel.updateProfilePhotoNew(it1) }
//        }
//    }

    // update display_name
    private fun logout() {
        binding?.apply {
            btnLogout.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                //Hawk.put(KEY_LOGIN, false)
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }
    }

    private fun changePassword() {
        binding?.apply {
            btnChangePassword.setOnClickListener {
                val intent = Intent(context, ChangePassword::class.java)
                startActivity(intent)
            }
        }
    }
}