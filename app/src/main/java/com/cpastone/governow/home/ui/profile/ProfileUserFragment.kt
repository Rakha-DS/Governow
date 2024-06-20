package com.cpastone.governow.home.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.capstone.governow.R
import com.cpastone.governow.api.ApiConfig
import com.capstone.governow.databinding.FragmentProfileUserBinding
import com.cpastone.governow.data.respone.UserProfileResponse
import com.cpastone.governow.home.ui.edit.EditProfileActivity
import com.cpastone.governow.home.ui.login.LoginScreenViewModel
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileUserFragment : Fragment() {
    private val viewModel by viewModels<ProfileViewModel> {
        com.cpastone.governow.home.ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var binding: FragmentProfileUserBinding
    private lateinit var tvNameProfile: TextView
    private lateinit var imgAvatarProfile: CircleImageView
    private lateinit var token: String



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileUserBinding.inflate(inflater, container, false)

        tvNameProfile = binding.tvNameProfile
        imgAvatarProfile = binding.imgAvatarProfile


        binding.fabEditProfile.setOnClickListener {
            navigateToEditProfile()
        }

        binding.btnLogout.setOnClickListener {
            logoutUser()
        }
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user != null) {
                token = user.token.toString()

                val profile = viewModel.getProfile(token)
                if (profile != null) {
                    tvNameProfile.setText(profile.data.username)
                    Glide.with(imgAvatarProfile.context)
                        .load(profile.data.profile_image)
                        .placeholder(R.drawable.ic_person)
                        .error(R.drawable.ic_person)
                        .into(imgAvatarProfile)
                }
            }
        }

        loadUserProfile()


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // Reload the user profile when the fragment is resumed
        loadUserProfile()
    }

    private fun loadUserProfile() {
        val call = ApiConfig.apiInstance.getUserProfile()
        call.enqueue(object : Callback<UserProfileResponse> {
            override fun onResponse(
                call: Call<UserProfileResponse>,
                response: Response<UserProfileResponse>
            ) {
                Log.d("respons", response.toString())
                if (response.isSuccessful) {
                    val userProfile = response.body()?.data
                    binding.tvNameProfile.text = userProfile?.username
                    Glide.with(this@ProfileUserFragment)
                        .load(userProfile?.profileImage)
                        .placeholder(R.drawable.ic_person)
                        .into(binding.imgAvatarProfile)
                } else {
                    Log.d("tessaas", "kntl")
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                // Handle the failure
            }
        })
    }

    private fun navigateToEditProfile() {
        val intent = Intent(requireContext(), EditProfileActivity::class.java)
        startActivity(intent)
    }

    private fun logoutUser() {
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        val intent = Intent(requireContext(), LoginScreenViewModel::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}

