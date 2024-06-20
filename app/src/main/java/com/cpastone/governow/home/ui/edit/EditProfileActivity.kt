package com.cpastone.governow.home.ui.edit

import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstone.governow.R
import com.capstone.governow.databinding.ActivityEditProfileBinding
import com.cpastone.governow.data.request.UpdateProfileRequest
import com.cpastone.governow.home.ui.profile.ProfileUserFragment
import com.google.android.material.textfield.TextInputEditText
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import androidx.fragment.app.commit

class EditProfileActivity : AppCompatActivity() {
    private val viewModel by viewModels<EditProfileViewModel> {
        com.cpastone.governow.home.ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var fullnameEdit: TextInputEditText
    private lateinit var usernameEdit: TextInputEditText
    private lateinit var emailEdit: TextInputEditText
    private lateinit var profileEdit: CircleImageView
    private lateinit var token: String
    private lateinit var saveButton: Button
    private var currentUri: Uri? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .into(binding.imgAvatar)

            currentUri = uri
            Log.d("hohom", currentUri.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fullnameEdit = binding.etFullName
        usernameEdit = binding.etUsername
        emailEdit = binding.etEmail
        profileEdit = binding.imgAvatar
        saveButton = binding.btnSave

        profileEdit.setOnClickListener {
            pickImage.launch("image/*")
        }

        viewModel.getSession().observe(this) { user ->
            if (user != null) {
                token = user.token.toString()

                val profile = viewModel.getProfile(token)
                if (profile != null) {
                    fullnameEdit.setText(profile.data.fullName)
                    usernameEdit.setText(profile.data.username)
                    emailEdit.setText(profile.data.email)
                    Glide.with(profileEdit.context)
                        .load(profile.data.profile_image)
                        .placeholder(R.drawable.ic_person)
                        .error(R.drawable.ic_person)
                        .into(profileEdit)
                }
            }
        }

        saveButton.setOnClickListener{
            saveButton.isEnabled = false
            val fullName = fullnameEdit.text.toString()
            val username = usernameEdit.text.toString()
            val email = emailEdit.text.toString()

            currentUri?.let { uri ->
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
                    Log.d("hohon", body.toString())
                    // Log the body
                    Log.d("hohon", "Body: $body")

                    val fullNameBody = fullName.toRequestBody("text/plain".toMediaTypeOrNull())
                    val usernameBody = username.toRequestBody("text/plain".toMediaTypeOrNull())
                    val emailBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
                    val insertStory = viewModel.updateProfile(token, UpdateProfileRequest(fullNameBody, emailBody, usernameBody, body))

                    // Log fullName, email, and username
                    Log.d("hohon", "Full Name: $fullName")
                    Log.d("hohon", "Email: $email")
                    Log.d("hohon", "Username: $username")

                    if(insertStory != null && insertStory.message != ""){
                        finish()
                    }else{
                        Toast.makeText(this, "Error, Fill all field", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            if(currentUri == null){
                val fullNameBody = fullName.toRequestBody("text/plain".toMediaTypeOrNull())
                val usernameBody = username.toRequestBody("text/plain".toMediaTypeOrNull())
                val emailBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
                val insertStory = viewModel.updateProfile(token, UpdateProfileRequest(fullNameBody, emailBody,usernameBody, null))

                if(insertStory != null && insertStory.message != ""){
                    finish()
                }else{
                    Toast.makeText(this, "Error, Fill all field", Toast.LENGTH_SHORT).show()
                }
            }
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
}