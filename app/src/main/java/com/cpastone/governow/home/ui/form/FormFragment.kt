package com.cpastone.governow.home.ui.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.capstone.governow.R
import com.capstone.governow.databinding.FragmentFormBinding
import com.cpastone.governow.api.ApiConfig
import com.cpastone.governow.data.model.Aspiration
import com.cpastone.governow.data.respone.GetAllAspirationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormFragment : Fragment() {

    private var _binding: FragmentFormBinding? = null
    private val binding get() = _binding!!
    private var dummyData: List<Aspiration> = listOf(
        Aspiration("title", R.drawable.sampel.toString(), "description", "date", "location", "category")
    )
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var aspirationAdapter: FormAdapter // Adapter for RecyclerView
    private lateinit var aspirationArrayAdapter: ArrayAdapter<CharSequence> // Adapter for Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFormBinding.inflate(inflater, container, false)

        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            fetchAspirations()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the spinner
        aspirationArrayAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.aspiration_array,
            android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinner.adapter = aspirationArrayAdapter

        // Set up RecyclerView
        binding.rvItemForm.layoutManager = LinearLayoutManager(context)
        aspirationAdapter = FormAdapter(dummyData, requireContext())
        binding.rvItemForm.adapter = aspirationAdapter

        // Spinner item selected listener
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterDataByCategory(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        fetchAspirations()
    }

    private fun fetchAspirations() {
        swipeRefreshLayout.isRefreshing = true

        // Perform the network request asynchronously using enqueue()
        ApiConfig.apiInstance.getAllAspirations().enqueue(object : Callback<GetAllAspirationResponse> {
            override fun onResponse(call: Call<GetAllAspirationResponse>, response: Response<GetAllAspirationResponse>) {
                swipeRefreshLayout.isRefreshing = false

                if (response.isSuccessful) {
                    val aspirationResponse = response.body()
                    if (aspirationResponse != null) {
                        dummyData = aspirationResponse.data
                        aspirationAdapter.updateData(dummyData)
                        aspirationArrayAdapter.notifyDataSetChanged()
                    } else {
                        // Handle null response or empty data
                    }
                } else {
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<GetAllAspirationResponse>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
                // Handle failure
            }
        })
    }

    private fun filterDataByCategory(position: Int) {
        val selectedCategory = aspirationArrayAdapter.getItem(position)
        if (selectedCategory != null && selectedCategory != "All Categories") {
            val filteredList = dummyData.filter { it.category == selectedCategory.toString() }
            aspirationAdapter.updateData(filteredList)
        } else {
            aspirationAdapter.updateData(dummyData)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



