package com.cpastone.governow.data.model

data class PostItem(
    val date: String,
    val like: Int,
    val caption: String,
    val userId: String,
    val polls: List<PollItem>?
)

data class PollItem(
    val voteCount: Int,
    val option: String
)

