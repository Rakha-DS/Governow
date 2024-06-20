package com.cpastone.governow.data.respone

import com.cpastone.governow.data.model.NewsItem

data class NewsResponse(
    val message: String,
    val data: List<NewsItem>
)