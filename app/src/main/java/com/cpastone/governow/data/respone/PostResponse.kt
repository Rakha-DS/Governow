package com.cpastone.governow.data.respone

import com.cpastone.governow.data.model.PostItem

data class PostResponse(
    val message: String,
    val data: List<PostItem>
)
