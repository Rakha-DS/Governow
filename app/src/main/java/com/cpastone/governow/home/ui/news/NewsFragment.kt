package com.cpastone.governow.home.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.capstone.governow.R
import com.cpastone.governow.api.ApiConfig
import com.cpastone.governow.data.model.NewsItem
import com.cpastone.governow.data.respone.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var searchView: SearchView
    private lateinit var adapter: NewsAdapter
    private var newsList: List<NewsItem> = emptyList()
    private var filteredNewsList: MutableList<NewsItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_news)
        recyclerView = view.findViewById(R.id.recycler_view_news)
        searchView = view.findViewById(R.id.search_view)

        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = NewsAdapter(filteredNewsList)
        recyclerView.adapter = adapter

        swipeRefreshLayout.setOnRefreshListener {
            fetchNews()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterNews(newText)
                return true
            }
        })

        fetchNews()

        return view
    }

    private fun fetchNews() {
        swipeRefreshLayout.isRefreshing = true

        ApiConfig.apiInstance.getAllNews().enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                swipeRefreshLayout.isRefreshing = false

                if (response.isSuccessful) {
                    val newsResponse = response.body()
                    if (newsResponse != null) {
                        newsList = newsResponse.data
                        filteredNewsList.clear()
                        filteredNewsList.addAll(newsList)
                        adapter.updateData(filteredNewsList)
                    } else {
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
            }
        })
    }

    private fun filterNews(query: String?) {
        filteredNewsList.clear()
        if (query.isNullOrEmpty()) {
            filteredNewsList.addAll(newsList)
        } else {
            for (newsItem in newsList) {
                val title = newsItem.title ?: ""
                val publisher = newsItem.publisher ?: ""

                if (title.contains(query, true) || publisher.contains(query, true)) {
                    filteredNewsList.add(newsItem)
                }
            }
        }
        adapter.updateData(filteredNewsList)
    }
}
