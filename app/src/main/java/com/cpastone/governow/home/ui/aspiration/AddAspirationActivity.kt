package com.cpastone.governow.home.ui.aspiration

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.capstone.governow.BuildConfig
import com.capstone.governow.R
import com.capstone.governow.databinding.ActivityAddAspirationBinding
import com.cpastone.governow.data.request.AspirationRequest
import com.cpastone.governow.data.request.UpdateProfileRequest
import com.cpastone.governow.home.HomeActivity
import com.google.android.material.textfield.TextInputEditText
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddAspirationActivity : AppCompatActivity() {
    private val viewModel by viewModels<AddAspirationViewModel> {
        com.cpastone.governow.home.ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityAddAspirationBinding
    private val calendar: Calendar = Calendar.getInstance()
    private lateinit var currentPhotoPath: String
    private var currentImageUri: Uri? = null
    private lateinit var titleEdit: TextInputEditText
    private lateinit var descriptionEdit: TextInputEditText
    private lateinit var dateEdit: TextInputEditText
    private lateinit var locationEdit: TextInputEditText
    private lateinit var buttonSave: Button



    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICK = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAspirationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edDate.setOnClickListener {
            showDatePickerDialog()
        }

        titleEdit = binding.edTitleAspiration
        descriptionEdit = binding.edDescription
        dateEdit = binding.edDate
        locationEdit = binding.edLocation
        buttonSave = binding.buttonSubmit

        binding.buttonSubmit.setOnClickListener {
            if (validateForm()) {
                buttonSave.isEnabled = false
                val title = titleEdit.text.toString()
                val description = descriptionEdit.text.toString()
                val date = dateEdit.text.toString()
                val location = locationEdit.text.toString()
                var success = false

                currentImageUri?.let { uri ->
                    val contentResolver: ContentResolver = applicationContext.contentResolver
                    val inputStream = contentResolver.openInputStream(uri)
                    val tempFile = File(cacheDir, "temp_image.jpg")
                    inputStream?.use { input ->
                        FileOutputStream(tempFile).use { output ->
                            input.copyTo(output)
                        }
                    }

                    val compressedPhoto = compressImage(tempFile)
                    compressedPhoto?.let { compressed ->
                        val requestFile = compressed.asRequestBody("image/jpg".toMediaTypeOrNull())
                        val body = MultipartBody.Part.createFormData("attachment", compressed.name, requestFile)

                        val titleBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
                        val descriptionBody = description.toRequestBody("text/plain".toMediaTypeOrNull())
                        val dateBody = date.toRequestBody("text/plain".toMediaTypeOrNull())
                        val locationBody = location.toRequestBody("text/plain".toMediaTypeOrNull())
                        val response = viewModel.createAspiration(AspirationRequest(titleBody, descriptionBody, dateBody, locationBody, body))

                        if(response != null && response.message != ""){
                            success = true
                        }else{
                            buttonSave.isEnabled = true
                            Toast.makeText(this, "Server Error: ${response?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                if(success){
                    Toast.makeText(this, "Form submitted successfully!", Toast.LENGTH_SHORT).show()
                    navigateToHome()
                }else{
                    buttonSave.isEnabled = true
                    Toast.makeText(this, "Error, Fill all field", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.buttonAttach.setOnClickListener {
            showImagePickerDialog()
        }
    }

    private fun compressImage(file: File): File? {
        try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(file.absolutePath, options)

            options.inSampleSize = calculateInSampleSize(options, 1024, 1024)

            options.inJustDecodeBounds = false
            val bitmap = BitmapFactory.decodeFile(file.absolutePath, options)

            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
            val fos = FileOutputStream(file)
            fos.write(byteArrayOutputStream.toByteArray())
            fos.flush()
            fos.close()

            return file
        } catch (e: Exception) {
            return null
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }


    private fun showDatePickerDialog() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }

        DatePickerDialog(
            this@AddAspirationActivity,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy" // Desired date format
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        binding.edDate.setText(sdf.format(calendar.time))
    }

    private fun validateForm(): Boolean {
        var isValid = true

        if (binding.edTitleAspiration.text.toString().trim().isEmpty()) {
            binding.edTitleAspiration.error = getString(R.string.error_field_required_tittle)
            isValid = false
        }

        if (binding.edDescription.text.toString().trim().isEmpty()) {
            binding.edDescription.error = getString(R.string.error_field_required_description)
            isValid = false
        }

        if (binding.edDate.text.toString().trim().isEmpty()) {
            binding.edDate.error = getString(R.string.error_field_required_date)
            isValid = false
        }

        if (binding.edLocation.text.toString().trim().isEmpty()) {
            binding.edLocation.error = getString(R.string.error_field_required_location)
            isValid = false
        }

        return isValid
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Select Option")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
            }
        }
        builder.show()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun openGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
            launchGallery()
        } else {
            requestGalleryPermission()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestGalleryPermission() {
        val permission = Manifest.permission.READ_MEDIA_IMAGES
        if (shouldShowRequestPermissionRationale(permission)) {
            Toast.makeText(this, "Gallery access is required to select an image.", Toast.LENGTH_SHORT).show()
        }
        requestPermissions(arrayOf(permission), REQUEST_IMAGE_PICK)
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            launchCamera()
        } else {
            requestCameraPermission()
        }
    }

    private fun requestCameraPermission() {
        val permission = Manifest.permission.CAMERA
        if (shouldShowRequestPermissionRationale(permission)) {
            Toast.makeText(this, "Camera access is required to take a photo.", Toast.LENGTH_SHORT).show()
        }
        requestPermissions(arrayOf(permission), REQUEST_IMAGE_CAPTURE)
    }

    private fun launchCamera() {
        val pictureFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            null
        }

        pictureFile?.also {
            val photoURI: Uri = FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.fileprovider", it)
            currentImageUri = photoURI

            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            }

            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            currentPhotoPath = absolutePath
            currentImageUri = Uri.fromFile(this)
        }
    }


    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val file = File(currentPhotoPath)
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    binding.ivItemPhoto.setImageBitmap(bitmap)
                    binding.ivItemPhoto.visibility = android.view.View.VISIBLE
                }
                REQUEST_IMAGE_PICK -> {
                    val selectedImageUri: Uri? = data?.data
                    selectedImageUri?.let {
                        currentImageUri = it
                        binding.ivItemPhoto.setImageURI(it)
                        binding.ivItemPhoto.visibility = android.view.View.VISIBLE
                    }
                }
            }
        }
    }

    private fun navigateToHome() {
        val activity = Intent(this, HomeActivity::class.java)
        startActivity(activity)
        finish()
    }
}
