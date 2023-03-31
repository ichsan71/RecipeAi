package com.cl.swafoody.ui.ingredients

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cl.swafoody.databinding.ActivityIngredientsBinding
import com.cl.swafoody.ml.Model
import com.cl.swafoody.ui.recipe.RecipeActivity
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.min

class IngredientsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIngredientsBinding
    private var imageSize = 32
    private lateinit var ingredient: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngredientsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnUploadPicture()
    }

    private fun btnUploadPicture() {
        binding.apply {
            btnUploadRecipe.setOnClickListener {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    val cameraIntent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(cameraIntent, 1)
                } else {
                    requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //if (resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE) {
        binding.apply {
            if (resultCode == RESULT_OK) {
                if (requestCode == 3) {
                    var image = data!!.extras!!["data"] as Bitmap?
                    val dimension = min(image!!.width, image.height)
                    image = ThumbnailUtils.extractThumbnail(image, dimension, dimension)
                    ivBack.setImageBitmap(image)
                    image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false)
                    classifyImage(image)
                } else {
                    val dat = data!!.data
                    var image: Bitmap? = null
                    try {
                        image = MediaStore.Images.Media.getBitmap(contentResolver, dat)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    ivResult.setImageBitmap(image)
                    image = Bitmap.createScaledBitmap(image!!, imageSize, imageSize, false)
                    classifyImage(image)
                }
            }
        }

        binding.btnGetRecipe.setOnClickListener {
            val intent = Intent(this, RecipeActivity::class.java)
            intent.putExtra(RecipeActivity.EXTRA_NAME, ingredient)
            startActivity(intent)
        }
    }

    private fun classifyImage(image: Bitmap) {
        try {
            val model = Model.newInstance(applicationContext)

            // Creates inputs for reference.
            val inputFeature0 =
                TensorBuffer.createFixedSize(intArrayOf(1, 32, 32, 3), DataType.FLOAT32)
            val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
            byteBuffer.order(ByteOrder.nativeOrder())
            val intValues = IntArray(imageSize * imageSize)
            image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
            var pixel = 0
            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for (i in 0 until imageSize) {
                for (j in 0 until imageSize) {
                    val `val` = intValues[pixel++] // RGB
                    byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 1))
                    byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 1))
                    byteBuffer.putFloat((`val` and 0xFF) * (1f / 1))
                }
            }
            inputFeature0.loadBuffer(byteBuffer)

            // Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer
            val confidences = outputFeature0.floatArray
            // find the index of the class with the biggest confidence.
            var maxPos = 0
            var maxConfidence = 0f
            for (i in confidences.indices) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i]
                    maxPos = i
                }
            }
            val classes = arrayOf(
                "carrot",
                "corn",
                "cucumber",
                "eggplant",
                "ginger",
                "lemon",
                "onion",
                "potato",
                "tomato")
            binding.tvResult.text = classes[maxPos]

            ingredient = classes[maxPos]

            // Releases model resources if no longer used.
            model.close()
            binding.btnGetRecipe.visibility = View.VISIBLE
            binding.btnGetRecipe.setOnClickListener {
                val intent = Intent(this, IngredientsActivity::class.java)
                startActivity(intent)
            }
        } catch (e: IOException) {
            // TODO Handle the exception
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        binding.ivBack.setOnClickListener { onBackPressed() }
    }
}