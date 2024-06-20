package com.cpastone.governow.data.model

data class PostItem(
    val date: String,
    val like: Int,
    val caption: String,
    val userId: String,
    val polls: List<PollItem>?,
    val id: String
)

data class PollItem(
    val voteCount: Int,
    val option: String
)

