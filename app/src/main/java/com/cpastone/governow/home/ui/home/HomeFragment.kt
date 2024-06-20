package com.cpastone.governow.home.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.capstone.governow.databinding.FragmentHomeBinding
import com.cpastone.governow.api.ApiConfig
import com.cpastone.governow.data.model.PollItem
import com.cpastone.governow.data.model.PostItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {
    private val viewModel by viewModels<HomeViewModel> {
        com.cpastone.governow.home.ViewModelFactory.getInstance(requireContext())
    }
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val defaultData = listOf(
        PostItem(
            date = "2024-06-20",
            like = 0,
            caption = "Pembangunan Infrastruktur",
            userId = "Walikota Makassar",
            polls = listOf(
                PollItem(option = "Bangun Gedung", voteCount = 0),
                PollItem(option = "Jalan tol", voteCount = 0),
                PollItem(option = "Bangun Jembatan", voteCount = 0)
            ),
            id = ""
        ),
        PostItem(
            date = "2024-06-20",
            like = 0,
            caption = "Apakabar masyarakat Makassar",
            userId = "Walikota Makassar",
            polls = null,
            id = ""
        ),
        PostItem(
            date = "2024-06-20",
            like = 0,
            caption = "Pemerintah ingin mengadakan gotong royong bersama masyarakat Makassar",
            userId = "Walikota Makassar",
            polls = listOf(
                PollItem(option = "Setuju", voteCount = 0),
                PollItem(option = "Tidak Setuju", voteCount = 0)
            ),
            id = ""
        ),
        PostItem(
            date = "2024-06-20",
            like = 0,
            caption = "Coba tebak siapa nama walikota Makassar saat ini?",
            userId = "Walikota Makassar",
            polls = null,
            id = ""
        )
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        swipeRefreshLayout = binding.swipeRefreshHome

        swipeRefreshLayout.setOnRefreshListener {
            setupRecyclerView()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.welcomeUsername.text = "Hi, " + viewModel.getSession().fullName
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiConfig.apiInstance.getAllPosts().execute()
                }

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.data != null) {
                        val itemList = responseBody.data
                        val adapter = MixedAdapter(itemList)
                        binding.rvItemVote.layoutManager = LinearLayoutManager(requireContext())
                        binding.rvItemVote.adapter = adapter
                    } else {
                        setupDefaultAdapter()
                    }
                } else {
                    setupDefaultAdapter()
                }
            } catch (e: Exception) {
                setupDefaultAdapter()
            } finally {
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun setupDefaultAdapter() {
        val adapter = MixedAdapter(defaultData)
        binding.rvItemVote.layoutManager = LinearLayoutManager(requireContext())
        binding.rvItemVote.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}